# 함수형 프로그래밍 기법
## 함수는 모든 곳에 존재한다.

```text
함수가 입력값 + 함수 내 지역변수만으로 결과값을 내며,
전역 변수라는 개념이 없고
입력값을 변경하지 않는다면...

함수 자체가 일종의 변수 아닐까?

그래서 나온 개념이 바로 **일급함수**
```

### 자바는 어떻게 일급함수를 지원하는가?

1. 고차원 함수 (Higher-Order Function)
📌 정의 : 함수를 인자로 받거나, 함수를 반환하는 함수  
이 두 가지 중 하나라도 만족하면 고차원 함수임.  

1️⃣ 함수를 인자로 받는 경우  
자바 8 이후에는 함수 자체를 넘길 수 있음.  
(정확히는 함수형 인터페이스를 넘김)  

```
Function<Integer, Integer> f = x -> x + 1;

public static int applyTwice(Function<Integer, Integer> f, int x) {
    return f.apply(f.apply(x));
}
```
✔ Function을 파라미터로 받는다 → 고차원 함수  

💡 왜 중요하냐?
고차원 함수가 있으면  
* 로직을 재사용할 수 있음 
* 동작을 파라미터화할 수 있음 
* 조건/전략을 함수로 전달 가능

2. 커링 
> 다중 인자 함수를 인자 하나짜리 함수들의 체인으로 바꾸는 것

ex. “가격에 세율을 적용해서 최종 가격을 계산한다”
* 커링이 적용된 경우
```java
public static double calculate(double price, double taxRate) {
    return price * (1 + taxRate);
}

double result = calculate(100, 0.1); // 110
```

* BiFunction 이라는 다중인자 전용 인터페이스를 사용하면?
* (함수 정의를 아래처럼 선언과 함께 하고, apply라는 메서드를 써서 실행한다.)
```java
BiFunction<Double, Double, Double> calculate =
        (price, taxRate) -> price * (1 + taxRate);

double result = calculate.apply(100.0, 0.1);
```

🚨 문제 : 세율이 항상 10%라면?  
👉 계속 0.1을 넘겨야 함  
👉 세율이 “설정값”인데 매번 전달해야 함  
👉 함수 조합하기 애매함 (BiFunction은 Stream에서 바로 쓰기 불편)  
그래서 커링을 써보면...
```text
Function<Double, Function<Double, Double>> calculate =
        taxRate -> price -> price * (1 + taxRate);
        
Function<Double, Double> tax10Calculator = calculate.apply(0.1);

double r1 = tax10Calculator.apply(100.0); // 110
double r2 = tax10Calculator.apply(200.0); // 220
```

💡 정리 : 커링을 왜 쓰냐?
* 일부 인자를 고정한 함수를 만들 수 있음 
* 함수 조합이 쉬워짐 
* 설정된 함수(설정값 고정) 만들기 좋음

예:
* 세율 고정 
* 할인율 고정 
* 환율 고정

이게 “설정된 전략 함수”를 만드는 방식임.

## 영속 자료구조
* 데이터를 수정할 때 기존 데이터를 변경하지 않고 새로운 버전을 만드는 자료구조를 영속 자료구조라고 한다.
* 업데이트 → 새로운 자료구조 생성 이라는 뜻!

일반 방식
```text
list = [1,2,3]

list.add(4)

list = [1,2,3,4]
```

영속 자료구조
```text
list1 = [1,2,3]

list2 = add(list1, 4)

list1 = [1,2,3]
list2 = [1,2,3,4]
```

### ❖ 중요한 특징 (진짜 핵심)

영속 자료구조는 복사(=깊은 복사)를 많이 하지 않는다.

대신 구조 공유 (structural sharing)를 사용한다.

구조 공유란? 새 자료구조를 만들 떄 같은 구조를 공유(=얕은 복사)한 뒤 

새로운 걸 추가.

특정 자료구조에서 이는 굉장히 유용

ex. 트리
* 리스트에서는 차이가 작아 보이나, 트리에서는 엄청 커진다. 
* 예를 들어 아래와 같은 트리가 있다고 하자.
```text
        8
      /   \
     3     10
    / \
   1   6
```

* 여기서 6을 7로 업데이트한다고 하면, 
  * 복사 (깊은 복사)면 모든 노드를 새로 만들지만, 
  * 구조 공유 (structural sharing)는 일부만 새로 만든다.
* 새로 만드는 건 다음과 같은 3개다 : 8', 3', 7

```text
        8'
      /   \
     3'    10
    / \
   1   7
```

️❖ 중요한 포인트

자료구조의 진짜 장점은 성능이 아니라...

내가 만든 모든 자료구조를 동시에 사용할 수 있다.
```text
version1
version2
version3
```
따라서,
* concurrency 
* undo 
* snapshot
* time travel debugging

같은 것들이 가능하다.

### 코드 예제 1
* [코드 1](../../main/java/part19/example1/TrainJourney.java)
* [코드 2](../../main/java/part19/example1/PersistentTrainJourney.java)
* 링크리스트를 영속 자료구조로 만들기 vs 링크리스트를 파괴적 자료구조로 만들기
* 영속 자료구조로 만들기에 쓴 함수가 `append()`
  * append로 appended, appended2를 만들었을 때 결과는 항상 같음.
  * 그러나 appended, appended2는 다른 자료구조. (메모리 주소가 다름)
* 파괴적 자료구조로 만들기에 쓴 함수가 `link()`
  * link를 두 번 실행하면 문제가 생긴다; 순환구조 발생

### 코드 예제 2
* [코드](../../main/java/part19/example2/PersistentTree.java)
* 해당 트리는 이진 탐색 트리 (BST).
* main에서 생성된 트리는 이렇게 생겼다
```text
        Mary:22
       /      \
 Emily:20    Tian:29
   /    \       /
Alan:50 Georgie:23 Raoul:23
```

* 영속 자료구조로 업데이트하면 : 기존 트리 t는 그대로 유지된다
* 파괴적 자료구조로 업데이트하면 : 기존 트리 t를 수정.

## 스트림과 게으른 평가
* 소수 생성기를 스트림을 이용해 만들어보자.
* [코드1](../../main/java/part19/example3/CalculatePrime.java)
* [코드2](../../main/java/part19/example3/MyMathUtil.java)

* 이 알고리즘은 별로다. 왜?
  * 너무 많은 수를 검사함
  * isPrime 자체도 비쌈. $O(n^2)$ 수준
  * 이전 소수 정보를 활용하지 않음.

* Stream API를 이용해 좀 더 개선된 코드를 구현해보자
* 에라스토테네스의 체를 구현한다고 보면 된다.
* [개선된 코드](../../main/java/part19/example3/CalculatePrime2.java)

* 그런데, 에러가 뜬다.
* 왜? Java Stream의 구조 때문
* Java Stream은 한번만 소비 가능한데, 여기서는 여러 번 소비하는 구조로 되어 있다.
```text
public static void main(String[] args) {
    IntStream numbers = numbers();
    int head = head(numbers);
    IntStream filtered = tail(numbers).filter(n -> n % head != 0);
    primes(filtered).forEach(System.out::println);
    
    // head(), tail() 같은 것이 스트림에 없기에 이렇게 나눠서 써야 함.
}
```

* 따라서 새로운 대안이 필요하다.
* 우선 [인터페이스 MyList](../../main/java/part19/example4/MyList.java)를 만드고
* 이걸 활용한 [Empty](../../main/java/part19/example4/Empty.java), 
* [MyLinkedList](../../main/java/part19/example4/MyLinkedList.java) 클래스,
* [LazyList](../../main/java/part19/example4/LazyList.java) 클래스를 만들어보자
* main()에 해당하는 [CalculatePrime3](../../main/java/part19/example4/CalculatePrime3.java) 도 만들자.

* 