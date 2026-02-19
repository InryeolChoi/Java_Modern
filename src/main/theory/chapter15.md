# CompletableFuture와 리액티브 프로그래밍의 기초
## 자바에서의 동시성
### 왜 '동시성'인가?
1. 멀티코어 cpu의 등장
2. 대규모 인터넷 기반 앱의 등장

### 자바에선, 어떻게 '동시성'을 구현하는가?
> 책에는 없는 내용이지만, 한번 쭉 정리하자!

#### 1. 멀티프로세싱 
- 프로세스 = 독립된 메모리 공간. IPC가 필요하고, 컨텍스트 스위칭 비용이 큼
- JVM 자체가 이미 프로세스라, 프로세스를 여러 개 굴리는 건 너무 무겁다.
- 따라서 자바에서는 주류가 되지 못함.

#### 2. 멀티쓰레딩
- 하나의 프로세스 안에서 메모리 공유
- IPC 필요 없으나, 다음과 같은 문제가 있음.
  1) race condition
  2) deadlock
  3) starvation
- 또 여러가지 문제가 있으니...우선 발전 과정에 따라 하나하나 정리해보자!

#### 3. 멀티쓰레딩 (1) : Runnable + Thread 클래스
* 자바 1.0 버전에서 나옴.
* Runnable 클래스 : 쓰레드가 할 일을 정한다.
* Thread 클래스 : 실제 쓰레드
- 인스턴스화를 할 때, Runnable 타입의 객체를 파라미터로 담아 쓰레드로 만든다.
- 이렇게 분리를 하면 쓰레드 재사용, 실행 방식 조정, 상속 문제가 모두 해결되기 때문!

* 예시코드 : 외부 인터넷 주소에서 값을 받아, 다음을 계산!
  * API 안에는 `completed`라는 필드가 있고 true/false의 값이 들어간다.
  * 최종결과 : 성공 요청 수, completed=true 개수
  * 결과(성공 요청 수, completed=true 개수)를 계산하는 로직은 [다음과 같다](../../main/java/part15/section1/ResultCounter.java)
  * 쓰레드 동작은 [다음 코드를 보면 된다.](../../main/java/part15/section1/ApiTask.java)
    * 쓰레드 동작 안에 결과 계산 로직이 포함되어 있다.
  * Main문은 [다음과 같다.](../../main/java/part15/section1/Main.java)

* 문제점 :
  1. Thread를 직접 생성함 -> 비용이 커짐.
  2. 쓰레드 생명주기를 개발자가 직접 관리 -> 제어 포인트가 코드 전체에 흩어짐
  3. 결과(성공 요청 수, completed=true 개수)를 모으는 부분이 뮤택스로 묶여 있음 -> 데드락이 걸릴 수 있음.
  4. 예외 처리(Try-catch) 지옥 -> 어떤 쓰레드에서 실패했는지 원인을 알기 힘듬
  5. 요청이 늘어나면 쓰레드 생성이 폭발 -> 뮤택스/세마포어는 쓰레드들이 한번에 자원에 터치하는 걸 막아줄 뿐, 쓰레드가 태어나는 걸 막을 순 없음

#### 3. 멀티쓰레딩 (2) : Callable + ExecutorService + Future
* Callable / ExecutorService 모두 자바 5에서 등장
* Callable 클래스 : **쓰레드가 할 일을 정하고...추가적인 장점이 있다!**
  * 반환값이 있다! 값을 엄마 쓰레드로 가지고 올 수 있다 = 뮤택스/세마포어를 줄일 수 있다.
  * 예외처리가 가능하다!
* ExecutorService 클래스 : **쓰레드풀이란 걸 사용할 수 있다**
  * 요청이 생길 때마다 쓰레드 만들기 X
  * 쓰레드를 미리 갯수만큼 만들고, 필요할 때 쓰레드를 재활용!
* Future 클래스 : **값을 받아오는 클래스**
  * 다른 쓰레드에서 실행 중이거나, 아직 끝나지 않은 작업의 결과를 현재 쓰레드가 안전하게 다루기 위한 연결고리
  * Callable은 ‘무엇을 반환할지’만 말해줄 뿐 ‘그 반환값을 어떻게 전달할지’는 해결 못 한다.
  * 결국 Callable + Future 조합으로 쓸 수 밖에 없다.

#### 4. 멀티쓰레딩 (3) : CompletableFuture
* Future의 단점 : 블로킹이기 때문에 main thread가 멈춤.
  * 멀티쓰레딩 (1) : 동기 (작업을 메인쓰레드가 책임) + 블로킹 (결과를 받을 때 메인쓰레드가 멈춤)
  * 멀티쓰레딩 (2) : 비동기 (작업을 서브쓰레드가 책임) + 블로킹 (결과를 받을 때 메인쓰레드가 멈춤)
  * 멀티쓰레딩 (3) : 비동기 (작업을 서브쓰레드가 책임) + 논블로킹 (결과를 받을 때 메인쓰레드가 안 멈춤)

```text
* 작업을 메인쓰레드가 책임 : 쓰레드의 시작, 끝을 모두 메인쓰레드가 통제 
* 작업을 서브쓰레드가 책임 : 쓰레드의 끝은 각 서브쓰레드가 통제
```

* CompletableFuture : 비동기 작업의 “완료 시점”을 조합하는 도구
* 작업을 바로 실행하고 중간에 get()/join()으로 기다리지 않고 완료되면 이어서 실행하도록 연결
* 따라서 체인 중간에는 블로킹이 없으며 이는 I/O 작업에서 유리함
  * 단, 마지막 작업에서는 join()을 사용. 이때 메인쓰레드에서 블로킹이 존재한다.
  * 또한 작업 자체가 블로킹 I/O라면, 서브쓰레드 안에서 블로킹이 존재.
* **즉, CompletableFuture는 블로킹을 제거하는 기술이 아니라 기다림을 ‘뒤로 미루는’ 조합 기술.**


#### 5. 멀티쓰레딩 (4) : 리액티브 프로그래밍
* 이산 데이터 vs 연속 데이터
  * 위에서의 모든 경우는 데이터가 이산적임 = API 호출을 셀 수 있음.
  * 클릭 한번에 api 한번. 
* 하지만, 만약...
  * 채팅서버라면?
  * 계속 업데이트되는 주식 가격이라면?
  * Kafka 메시지 스트림이면?
* 이런 경우에는 리액티브 프로그래밍을 쓴다.


>> 인제 본격적으로 책을 파보면서 공부를 해보자.

## Executor와 쓰레드 풀
> Executor와 쓰레드 풀을 좀 더 이론적으로 자세히 보자

### 1. ExecutorService로 쓰레드 풀 만들기
```java
import java.util.concurrent.*;
ExecutorService executor = Executors.newFixedThreadPool(n);
// n은 쓰레드 갯수
```

### 2. 쓰레드풀은 항상 좋은가?
- 거의 모든 상황에서 그렇다.
- 하지만, 블로킹 I/O를 사용할 경우 문제가 생긴다.
  - `String data = socket.read();` -> 이런 코드에서 쓰레드는 아무 일을 하지 않는다.
  - 그러나 쓰레드 풀에서는 점유 중이다.
  - 따라서 쓰레드가 고갈된다.

### 3. 쓰레드의 다른 추상화 : 중첩되지 않은 메서드 호출
* [7장의 병렬 스트림 & fork/join 프레임워크](./chapter7.md)를 다시 한번 보자.
  * 쓰레드 `fork()` + `join()` 을 하나의 메서드에서 관리
  * 즉 자식 쓰레드의 생성, 작동, 회수를 모두 엄마 쓰레드가 활용하는 것
  * 위에서 본 것처럼 이러한 방식은 동기(synchronous)라고 한다.
* 반대로 자식 쓰레드가 알아서 작동, 회수를 관리하는 방법도 있다
  * 위에서 본 것처럼 이러한 방식을 비동기(asynchronous)라고 한다.
* 이러한 비동기 멀티쓰레딩은 다음과 같은 위험성이 있다
  1. 데이터 경쟁의 문제 (race condition)
  2. 엄마 쓰레드의 갑작스러운 종료 -> 자식 쓰레드는 어떻게 회수되어야 하는가?
  * 2번 문제를 해결하기 위한 방법으로 자식 쓰레드에 `setDaemon()`을 걸 수 있다.
* 우리가 볼 future, CompletableFuture이 바로 비동기 멀티쓰레딩

### 4. 쓰레드에 무엇을 바라는가?
* 결국 쓰레드에게 바라는 것은 병렬성의 장점을 극대화하도록 프로그램 구조를 만드는것
* 프로그램을 작은 단위로 쪼개는 것

## 동기 API vs 비동기 API
* 함수 f와 g가 있다고 해보자. [(코드)](../../main/java/part15/ApiExample/Functions.java)
  * f와 g는 생각보다 복잡한 연산을 수행한다.
* 먼저 동기 방식의 멀티쓰레딩으로 f + g를 계산한 경우를 보자. [(코드)](../../main/java/part15/ApiExample/ThreadExample.java)
  * 코드가 좀 복잡한 면이 있다.
  * 자식쓰레드의 흐름을 모두 엄마쓰레드가 정한다 = 직접 자식쓰레드의 모든 것을 제어해야 한다.
* 그래서 다음과 같은 대안이 제시된다.
* 1. Future를 이용한 f + g를 계산한 경우. [(코드)](../../main/java/part15/ApiExample/ExecutorServiceExample.java)
  * 자식쓰레드의 흐름을 모두 엄마쓰레드가 정하지는 않는다 = 결과를 받는 것까지 직접 제어하지 않는다.
  * 사실 f에만 Future를 적용할 수도 있지만, API 스타일을 통일하는 것이 좋고 병렬 하드웨어가 프로그램을 최대한 빠르게 실행하도록 하려면, (적절한 한도 내에서) 더 작고 더 많은 작업(task)으로 나누는 것이 유리하다.
* 2. 람다를 활용한 콜백 스타일
  * 콜백 스타일로 f와 g를 따로 계산해보자. [(코드)](../../main/java/part15/ApiExample/CallbackStyleExample.java)
  * 단, 이 코드는 앞의 Future 방식처럼 f와 g의 합을 만들기 쉽지 않다.
  * f -> g로 하면? 병렬 실행이 아니라 “순차 실행" + 콜백지옥 시작.
  * 결과를 모으는 공통 지점이 자연스럽게 존재하지 않기 때문에 계속 괄호를 넣어야 한다.

```text
f(x, y -> {
    g(x, z -> {
        System.out.println(y + z);
    });
});
```

>> 아니 Future 쓰면 되지. 왜 콜백 스타일이 등장했지? 
>> 굳이 람다까지 끌고 와야 돼?

왜 그런지 그 대답을 밑에서 알아보자.

### sleep() 같은 블로킹 연산은 해롭다
* 사람이나, 동작 속도를 제한해야 하는 애플리케이션과 상호작용할 때, 자연스럽게 떠오르는 방법 중 하나는 sleep() 메서드를 사용하는 것이다.
* 하지만 문제는 다음과 같다:

>> sleep 중인 스레드는 여전히 시스템 자원을 점유한다.

스레드가 몇 개 안 된다면 큰 문제가 아닐 수 있다.  
그러나 스레드가 많고, 그중 대부분이 잠들어 있다면 문제가 된다.  
따라서, 블로킹을 최소화하는 구조로 설계할 필요가 있다.  
* Future는 비동기 API지만, get()을 호출하면 블로킹된다.
* 콜백 스타일은 결과가 준비되었을 때 실행되므로, 블로킹 없이 이어가는 구조를 만들 수 있다.

**그러나, 모든 걸 비동기로 만드는 건 이상적이지만 현실은 복잡하다.**  
(앞에서 봤던 콜백 스타일이 어땠는지 생각해보자.)  
그래서 등장한 것이 바로 콜백 스타일을 추상화한 CompletableFuture.  
CompletableFuture은 16장에서 자세히 보자.

## 박스 - 채널모델
>> Future의 한계를 좀 더 짚어보자.

동시성 시스템을 그림으로 그린 것을 box-and-channel 모델이라고 한다.
* 아까 짰던 f + g 같은 케이스도 포함이다.

1. 순차 코드 방식 : 전혀 병렬성을 활용하지 못한다.
2. Future 이용 : 여러 개의 box-and-channel 다이어그램 존재.
* 많은 작업이 get()으로 기다리게 됨
* 따라서 하드웨어 병렬성이 충분히 활용되지 못하고, 심하면 데드락 걸림

그래서 자바는 combinator에게 맡기는 구조를 채택하는 방향을 도입.  
이것이 바로 CompletableFuture.

## CompletableFuture와 동시성을 위한 Combinator
>> 자바 8은 CompletableFuture를 통해 Future를 조합(composition)할 수 있게 함

CompletableFuture는 실행할 코드를 처음부터 줄 필요가 없음.  
```text
CompletableFuture<T> cf = new CompletableFuture<>();
```
나중에 다른 쓰레드가 값을 채워줌. 그래서 “Completable”임.  
```
cf.complete(value);
```

일단 f + g 코드를 CompletableFuture로 한번 써보자.  
```text
CompletableFuture<Integer> b = new CompletableFuture<>();
executorService.submit(() -> b.complete(g(x)));
int a = f(x);
System.out.println(a + b.get());
```

근데 어쨌든 블로킹이 생길 수 있다.  
어디에서? `int a = f(x)`에서 f가 g보다 느리다면..  
그래서 ConpletableFuture은 스트림처럼 thenCombine()이라는 메소드를 제공한다
```text
CompletableFuture<Integer> c = a.thenCombine(b, (y, z) -> y + z);
```

모든 코드에서 get() 블로킹이 큰 문제는 아님.  
하지만, 여러 서비스 요청의 병렬 처리가 필요한 대규모 시스템에서는 CompletableFuture가 좋은 옵션이 되줄 수 있다.  

## 빌행자-구독자 모델과 리액티브 프로그래밍
>> 본격적으로 리액티브 프로그래밍으로 넘어가보자.  

**Future vs 리액티브**
* Future : 독립적으로 실행되는 하나의 계산
* 리액티브 : 시간이 지나면서 여러 번 값을 생성하는 Future-like 객체

**예제**
1. [SimpleCell 코드](../../main/java/part15/ReactiveExample/SimpleCell.java)
* SimpleCell 객체는 발행자이면서 구독자
* 또한 코드 구조는 다음과 같음.
1️⃣ 자기 값 업데이트
2️⃣ 자기 이름 출력
3️⃣ 구독자들에게 알림

* 따라서 main을 보면
```text
c1.subscribe(c3);
c1.onNext(10);
c2.onNext(20);
```
C3는 C1에만 구독.  
결과는 다음과 같음.
```text
C1:10
C3:10
C2:20
```
c3의 결과가 c1이 바뀌자마자 바뀜.

2.[ArithmeticCell 코드](../../main/java/part15/ReactiveExample/ArithmeticCell.java)
* 마찬가지로, 이벤트 기반 자동 갱신이 되는 구조.

**발행자 - 구독자 모델의 특징**
1. 구독자가 많아지면, 또는 발행자-구독자-구독자의 구독자 이런 구조가 나올 수 있음.
2. 발행자 <-> 구독자가 그래프 형태임을 알 수 있음.
3. 위쪽 발행자를 Upstream, 아래쪽 구독자를 Downstream이라고 함.

**Pressure와 Backpressure**
🚨 Pressure 문제린?
* 예:
  * SMS 10000개가 1초에 전송
  * 버퍼 overflow 
  * crash
* 이런 걸 pressure이라고 함.

🧱 Backpressure
* Pressure의 해결책
* 소비자가 처리 속도를 제어
* 그래서 Subscription 인터페이스가 등장!
```text
interface Subscription {
    void cancel();
    void request(long n);
}
```
* Subscriber에 추가된 다음 메서드를 통해 Subscription 인터페이스를 활용
```text
void onSubscribe(Subscription subscription);
```
* Publisher가 Subscription 객체를 생성하여 Subscriber에게 전달
* Subscriber는 request(n)을 호출하여 n개만 보내라라고 요청
* 통신사가 고객에게 요금제 선택권을 준다고 생각하면 쉬울듯.


* 구현 시 고민 사항 
  * 가장 느린 Subscriber 기준으로 보낼 것인가? 
  * Subscriber별 큐를 만들 것인가? 
  * 큐가 너무 커지면? 
  * 이벤트를 버릴 것인가?
-> 데이터 의미에 따라 결정됨.
  * 온도 하나 손실 → 괜찮음 
  * 은행 계좌 거래 하나 손실 → 치명적

## 리액티브 시스템 vs 리액티브 프로그래밍
점점 더 많이 reactive system과 reactive programming이라는 말을 듣게 될 텐데, 이 둘은 전혀 다른 개념임.

1️⃣ Reactive System이란?
> 런타임 환경 변화에 반응할 수 있도록 설계된 아키텍처를 가진 시스템

즉, “코드 스타일”이 아니라 시스템 전체 구조의 특성을 말하는 것.
Reactive system이 가져야 할 핵심 속성은:

🔹 1. Responsive (응답성)
* 시스템이 실시간으로 응답 가능해야 함 
* 누군가 큰 작업을 돌리고 있다고 해서 단순한 요청까지 느려지면 안 됨
👉 “큰 배치 작업 때문에 로그인 API가 느려지면 안 된다” 이런 느낌

🔹 2. Resilient (복원력)
* 일부 컴포넌트가 실패해도 시스템 전체가 무너지면 안 됨 
* 네트워크 링크 하나 끊어졌다고 모든 요청이 실패하면 안 됨 
* 응답 없는 컴포넌트는 다른 대체 컴포넌트로 우회 가능해야 함
👉 MSA에서 흔히 말하는 Circuit Breaker 같은 개념이 여기서 나옴

🔹 3. Elastic (탄력성)
* 워크로드 변화에 맞춰 시스템이 스스로 조절 가능해야 함

소프트웨어에서는:
* worker thread 수를 조절
* 리소스를 동적으로 재할당
* auto-scaling

2️⃣ Reactive Programming이란?
>> 메시지 기반으로 비동기 데이터 흐름을 처리하는 **프로그래밍 스타일**

* Java에서는 java.util.concurrent.Flow 인터페이스들이 이 스타일을 제공함.
* 이건 프로그래밍 스타일이자, 전체 시스템의 특징이 아님.

정리하자면 다음과 같음.
* Reactive system은 “어떤 시스템이 되어야 하는가” 
* Reactive programming은 “그걸 어떻게 구현할 것인가”