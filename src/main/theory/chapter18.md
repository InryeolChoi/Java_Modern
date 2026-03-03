# 함수형 관점으로 생각하기
## 선언형 프로그래밍
> 함수형 프로그래밍을 보기 전, 먼저 명령형과 선언형을 비교해보자.

* 명령형 : '어떻게'에 집중한다.
* 선언형 : '무엇을'에 집중한다.

명령형 예시  
```text
Transaction mostExpensive = transaction.get(0);
if (mostExpensive == null)
    throw new IllegalArgumentException("Empty list of Transactions");
    
for (Transaction t: transactions.subList(1, transactions.size())) {
    if (t.getValue() > mostExpensive.getValue()) {
        mostExpensive = t;
    }
}
```

선언형 예시  
```text
Optional<Transaction> mostExpensive = 
    transactions.stream()
                .max(comparing(Transaction::getValue));
```

## 함수형 프로그래밍이란?
* 우선, 함수형 프로그래밍에서의 함수란?
  * 0개 이상의 인수를 가진다
  * 1개 이상의 결과를 반환하나 부작용이 없다
* 부작용이 없다?
  * 입력이 같으면 항상 출력이 같아야 한다
  * 외부 상태를 변경하지 않는다

1️⃣프로그래밍에서 부작용이란:
> 🔥 함수가 자기 바깥의 상태를 변경하는 것

예를 들면 이런 것들이다.
```text
int total = 0;

void add(int value) {
    total += value;  // 🔥 외부 상태 변경
}
```
여기서 add()는 값을 계산하는 게 아니라
👉 외부 변수 total을 바꿔버린다.

이게 부작용이다.

대표적인 부작용들 
* 전역 변수 변경 
* 객체 내부 상태 변경 (setter 호출 등)
* 파일에 쓰기 
* DB에 insert 
* 콘솔 출력 (System.out.println)
* 예외 던지기 
* 네트워크 호출

즉, “계산 결과” 말고 다른 것을 건드리면 부작용 ❗

2️⃣ 그럼 함수형 프로그래밍이 말하는 “부작용 없는 계산”은?

함수형 프로그래밍에서는 함수가 이런 특성을 가져야 한다고 본다:  
🎯 입력이 같으면 항상 출력이 같아야 한다.  
🎯 외부 상태를 변경하지 않는다.  

이걸 순수 함수 (pure function) 라고 한다.

ex. 순수 함수
```text
int add(int a, int b) {
    return a + b;
}
```

ex. 순수하지 않은 함수
```text
int total = 0;

int add(int value) {
    total += value;
    return total;
}
```

이 함수는:
* 호출할 때마다 결과 달라짐 
* 외부 상태에 의존 
* 테스트 어려움 
* 멀티스레드에서 위험

❌ 함수형 프로그래밍이 싫어하는 형태

### 함수형 프로그래밍의 이점
1️⃣ 예측 가능성 (Predictability)
* 언제 호출해도 동일	
* 상태에 의존하지 않음	
* 추론 가능

👉 프로그램을 “수학적으로 reasoning” 할 수 있게 됨.

이게 가장 근본적인 이점이다.

2️⃣ 병렬성과 동시성에 강함

* 공유 상태 없음 
* 데이터가 불변
* 계산이 독립적

👉병렬화가 매우 쉬움.

👉따로 뮤택스라는 걸 만들 필요가 없음.   

그래서 함수형 언어/프레임워크가 분산/동시성에 강하다.
* Scala (Akka)
* Elixir (Erlang VM)
* Haskell 
* Clojure

3️⃣ 코드 조합성 (Composability)

함수형은 작은 함수들을 조합한다.



## 자바에서의 함수형 프로그래밍
> 결론부터 말하자면, 불가능하다.