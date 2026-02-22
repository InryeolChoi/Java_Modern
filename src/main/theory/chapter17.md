# 리액티브 프로그래밍 
* 왜 리액티브 프로그래밍인가? '시간에 따라 계속 값이 흘러오는 비동기'가 필요하다.
* Future / CompletableFuture의 한계 : 결과가 딱 1번.
* 리액티브 : 계속적인 결과가 이어짐

## 리액티브 매니패스토
* 리액티브 app와 시스템 개발의 핵심 원칙을 공식적으로 정의하는 모임.
* 여기서 정한 리액티브 app와 시스템 개발의 핵심 원칙은 다음과 같다.
1. 반응성 : 빠르게 응답
2. 회복성 : 장애에 강함
3. 탄력성 : 부하에 따라 확장
4. 메시지 주도 : 메시지 기반

**즉, 환경의 변화에 반응할 수 있는 구조가 바로 리액티브 App과 리액티브 시스템!**

### 리액티브 App과 리액티브 시스템
1. App 수준의 리액티브
>> 하나의 애플리케이션이 외부 자극에 반응하도록 설계되는 것

* 어플리케이션 수준이란? : 하나의 앱 구조
* 리액티브 App은 이벤트 주도.
* 이벤트란? “어떤 일이 발생했다”는 신호
  * ex. 버튼 클릭, Future 완료 등
  * 프로세스 내의 개념

2. 시스템 수준의 리액티브
> “시스템 전체를 어떻게 리액티브하게 만들 것인가?”

* 시스템 수준이란? : 여러 앱/노드/서비스 전체
* 리액티브 시스템은 메시지 주도.
* 메시지란? 컴포넌트 사이를 오가는 데이터 패킷
  * ex. Kafka 메시지, RabbitMQ 큐 메시지, HTTP 요청 등
  * 프로세스 간 개념
* 컴포넌트란? 독립적으로 배포되고, 실패 격리 가능한 단위
  * 리액티브 시스템은 컴포넌트 간의 관계를 어떻게 설계하느냐가 핵심
  * 너무 어렵게 생각하지 말고, 하나의 컨테이너라고 생각하자.

### 어떻게 리액티브 시스템을 잘 만들건데?
1. 고립 : 한 컴포넌트가 죽어도 전체는 살아있음
2. 비결합 : 서로를 직접 참조하지 않음.
어떻게 참조하지 않고 데이터를 주고 받을 것인가? 메시지로!
3. 위치 투명성 : 서비스가 어디 있는지 몰라도 됨

### 실제 코딩하는 방법은?
1. Java 9부터 지원하는 Flow 클래스 쓰기
2. 라이브러리 형태로 지원하는 RxJava 쓰기

## 리액티브 스트림 코딩하기 (1) : Flow api
* 스트림 : 시간에 따라 발생하는 이벤트의 흐름
* 스트림을 다루려면 다음 4가지가 꼭 필요함.
1. 비동기
2. 논블로킹
3. Backpressure 지원
4. 데이터 흐름 중심

### Flow 클래스 소개
>> Reactive Streams 표준을 JDK 안으로 가져옴

📦 Flow는 클래스가 아니라 “인터페이스 묶음”
```text
Flow
 ├─ Publisher<T>
 ├─ Subscriber<T>
 ├─ Subscription
 └─ Processor<T,R>
```

1️⃣ Flow.Publisher : 데이터를 발행
```text
public interface Publisher<T> {
    void subscribe(Subscriber<? super T> subscriber);
}
```

2️⃣ Flow.Subscriber : 데이터를 구독
해당 인터페이스는 콜백 기반 구조임.
```text
public interface Subscriber<T> {
    void onSubscribe(Subscription subscription);
    void onNext(T item);
    void onError(Throwable throwable);
    void onComplete();
}
```

3️⃣ Flow.Subscription : Subscriber가 “얼마나 받을지” 요청함.
이게 바로 backpressure.
```text
public interface Subscription {
    void request(long n);
    void cancel();
}
```~~~~~~

4️⃣ Flow.Processor<T,R> : 중간 변환자
스트림 파이프라인의 중간 노드
```text
public interface Processor<T,R>
    extends Subscriber<T>, Publisher<R>
```

따라서 전체 구조는 다음과 같음.

1️⃣ Subscriber가 Publisher에 subscribe()  
2️⃣ Publisher가 onSubscribe() 호출하면서 Subscription 전달  
3️⃣ Subscriber가 request(n) 호출
4️⃣ Publisher가 onNext() n번 호출
5️⃣ 완료되면 onComplete()  

### Flow로 실제 어플리케이션 만들어보기
1️⃣[TempInfo](../../main/java/part17/Example1/TempInfo.java)  
2️⃣[TempSubscription](../../main/java/part17/Example1/TempSubscription.java)  
3️⃣[TempSubscriber](../../main/java/part17/Example1/TempSubscriber.java)  
4️⃣[Main](../../main/java/part17/Example1/Main.java)  

1. Main에서 TempSubscriber를 구독자로 추가  
2. TempSubscriber.onSubscribe가 구독을 추가하고 첫 번째 요청을 보냄  
3. Main의 getTemp 작동해 응답 날림 -> TempSubscriber의 onNext 작동  
4. TempSubscriber의 onNext는 -> Subscription에 메시지를 날림  
5. Subscription은 TempInfo에서 가져온 정보를 TempSubscriber.onComplete()로 날림.  
6. 이 과정이 반복.  

다만, 아래 코드 중 if문에 걸린 내용 때문에 가끔씩 에러가 터진다.  
-> 약 1/10 확률로 에러가 터질수 밖에 없다.  
```text
    public static TempInfo fetch(String town) {
        if (random.nextInt(10) == 0)
            throw new RuntimeException("Error!");
        return new TempInfo(town, random.nextInt(100));
    }
```

그렇다고 if문을 지우면 에러가 난다. 왜? 다음과 같은 재귀 반복 현상이 생기기 때문.  
```text
subscription.request(1)
 └─ subscriber.onNext
     └─ subscription.request(1)
         └─ subscriber.onNext
             └─ subscription.request(1)
                 └─ subscriber.onNext
```

실제 무한한 스트림은 구독자가 무한히 request()를 보내지 않는다.  
발행자로부터 무한한 데이터가 오고, 구독자는 이를 끊어서 받는다.  
```text
(이벤트 발생)
     ↓
Publisher 내부 큐에 적재
     ↓
Subscriber가 request(n)
     ↓
가능한 만큼 drain
```

### Flow로 실제 어플리케이션 만들어보기 (2)
* 이번에는 실제 스트림 데이터를 활용해서 무한히 도는 어플리케이션을 만들어보자.
* 조건 : Processor가 들어가야 한다. 

> binance API를 사용, 실시간 비트코인 변동폭을 보여주는 App을 설계

1️⃣[Main](../../main/java/part17/Example2/Main.java)
2️⃣[BtcTradePublisher](../../main/java/part17/Example2/BtcTradePublisher.java)  
3️⃣[PriceChangeProcessor](../../main/java/part17/Example2/PriceChangeProcessor.java)  
4️⃣[PriceEvent](../../main/java/part17/Example2/PriceEvent.java)  
5️⃣[ChangeSubscriber](../../main/java/part17/Example2/ChangeSubscriber.java)

전체 구조는 다음과 같다.  
```text
Main.WebSocket (Binance 서버)
↓
BtcTradePublisher (Publisher<Double>)
- 외부 세계와 연결
- onOpen -> OnText (extractTradePrice 사용)
↓
PriceChangeProcessor (Processor<Double, PriceEvent>)
- 계산 파이프라인
- 중간에 PriceEvent를 이용해 변동률과 현재가를 묶는다.
↓
ChangeSubscriber (Subscriber<PriceEvent>)
- 출력
```

* 여기서는 Subscription이 직접 구현되지는 않음.
* 다만 PriceChangeProcessor 안에 SubmissionPublisher가 상속되어 있음.
* 따라서 Onsubscribe() 호출되면, Subscription이 알아서 등록된다.
* 즉 Subscription의 역할이 PriceChangeProcessor 안에 녹아있다.
```text
public class PriceChangeProcessor extends SubmissionPublisher<PriceEvent>
        implements Processor<Double, PriceEvent> {
...
    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }
...
}
```


### 자바는 왜 Flow Api 구현이 없는가?
* 왜 Flow는 인터페이스만 있을까?
-> 이미 Flow 등장 전부터 다양한 리액티브 스트림용 라이브러리가 존재했기 때ㅜㅁㄴ
-> Flow는 일종의 표준공식

## 리액티브 스트림 코딩하기 (2) : Rxjava 사용하기
* Rxjava : 가장 많이 사용하는 