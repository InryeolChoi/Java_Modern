# Completablefuture: 조합 가능한 비동기 프로그래밍

* 오늘날은 여러 개의 처리 자원(CPU 코어 등)이 존재하며, 
* 우리는 프로그램이 이 자원들을 최대한 고수준(high-level) 방식으로 활용하기를 원한다.
* 즉, 스레드를 직접 지저분하게 다루는 비구조적이고 유지보수 어려운 코드를 피하고자 한다.

우리는 다음을 이미 봤다:
* 병렬 스트림(parallel streams)
* fork/join 병렬성

이들은
* 컬렉션 반복 작업 
* 분할 정복(divide-and-conquer) 알고리즘
과 같은 경우에 고수준 병렬 표현을 제공한다.

하지만 메서드 호출 자체도 병렬 실행의 기회가 될 수 있다.  

Java 8과 9는 이를 위해 두 가지 API를 도입했다:
1.	CompletableFuture
2.	리액티브 프로그래밍 패러다임

이 장에서는 CompletableFuture를 실전코드로 다뤄보며 연습한다고 생각.

## Future 사용하기
### Future의 도입
Java는 미래의 어느 시점에 사용 가능해질 결과를 모델링하기 위해 Future를 도입
* 시간이 오래 걸리는 작업을 Future 안에서 실행시키면:
  * 호출한 스레드는 결과를 기다리지 않고 (비동기)
  * 다른 유용한 작업을 계속 수행할 수 있다. 

Future의 예시코드는 다음과 같다.
```text
// 쓰레드풀을 만든다.
ExecutorService executor = Executors.newCachedThreadPool();

// Future + executor + Callable을 이용
Future<Double> future = executor.submit(
    new Callable<Double>() {
        public Double call() {
            return doSomeLongComputation();
        }
    }
);

doSomethingElse();

// 결과를 받아오기 위해 기다림.
try {
    Double result = future.get(1, TimeUnit.SECONDS);
} catch (ExecutionException ee) {
    // the computation threw an exception
} catch (InterruptedException ie) {
    // the current thread was interrupted while waiting
} catch (TimeoutException te) {
    // the timeout expired before the Future completion
}
```

* 긴 작업은 ExecutorService가 제공하는 별도 스레드에서 실행된다. 
* 메인 스레드는 그동안 다른 작업을 수행할 수 있다.
* 더 이상 다른 작업을 할 수 없을 때 `future.get()`으로 결과를 가져온다.
* `future.get()`
  * 작업이 이미 끝났으면 즉시 반환
  * 아니면 메인 스레드를 블로킹하여 기다림

만약 그 긴 작업이 영원히 끝나지 않는다면?  
-> 다음과 같이 대기시간을 둘 수 있다.
```java
future.get(timeout, TimeUnit.SECONDS);
```
그러나...

### Future의 한계
Future로는 “의존성 표현”이 어렵다.  
예를 들어, 다음과 같은 것을 의미한다.  
> 긴 계산이 끝나면 그 결과를 다른 긴 계산에 전달하고
> 그 계산이 끝나면 또 다른 결과와 결합하라

그래서 다음과 같은 기능들이 추가로 필요하다.
1️⃣ 두 비동기 계산을 결합하기  
* 서로 독립적인 경우
* 두 번째가 첫 번째 결과에 의존하는 경우
2️⃣ 여러 Future가 모두 끝날 때까지 기다리기  
3️⃣ 여러 Future 중 가장 빠른 하나만 기다리기  
* 같은 값을 다른 방식으로 계산할 때 유용
4️⃣ Future를 프로그래밍적으로 완료시키기  
* 비동기 작업 없이 직접 결과를 넣기  
5️⃣ 완료에 반응하기
* Future가 끝났을 때 통지를 받고 블로킹 없이 후속 작업을 수행하기

## 예제코드 1 : 비동기로 설계해보기
>> 먼저, Future를 이용해 가격을 가지고 오는 메서드롤 만들자.

**[Shop 클래스](../../main/java/part16/Example1/Shop.java)**
* Shop 안의 물건 가격을 계산 후 반환하는 것이 목표
* 가격을 계산하는 메소드의 경우, 계산 시에 일부러 delay를 넣는다.
  * 코드 상에서 `calculatePrice()` 하단에 `delay()`를 넣음

* 반환하는 메서드를 다음과 같은 순으로 진화시킬 수 있다.
1. getprice() : 동기로 설계하기
2. getPriceAsync() : 비동기로 설계하기
3. getPriceAsync2() : Try-Catch로 오류처리를 추가
4. getPricesAsync3() : CompletableFuture.supplyAsync()로 단순화하기

2번, 3번에서 비동기를 구현할 때는 직접 비동기 멀티쓰레딩을 다음과 같이 했어야 했다.  
(이때 만들어지는 쓰레드는 1개)  
```text
new Thread(() -> {
    try {
        double price = calculatePrice(product);
        futurePrice.complete(price);
    } catch (Exception ex) {
        futurePrice.completeExceptionally(ex);
    }
}).start();
```

이를 4번과 같이 단순화 할 수 있다.  
(이때는 ForkJoinPool.commonPool()가 내부에서 돈다.)  
```text
CompletableFuture.supplyAsync(() -> calculatePrice(product));
```

**[ShopMain 클래스](../../main/java/part16/Example1/ShopMain.java)**
* 비동기 설계 시, 함수의 반환 (invocation)과 값의 반환의 속도가 다름을 보여줌
* 또한 메인메서드가 다른 작업을 함으로써 주요 작업(=가격 반환)의 흐름이 자식메서드로 감을 보여줌.
* **기다리는 시간을 낭비하지 않는다**는 비동기의 장점을 보여줌.

## 예제코드 2 : 비동기 + 논블로킹
>> CompletableFuture를 이용해 가격을 한번에 찾는 메서드를 만들어보자.

### 첫 번째 예시
**[BestPriceFinder 클래스](../../main/java/part16/Example2/BestPriceFinder.java)**
5가지의 Shop을 정의하고 각 Shop별 가격을 나열하는 메서드를 만들면 된다.  
1. findPrices() : stream()으로 각 Shop별 가격을 찾기
2. findPrices2() : parallelStream()으로 각 Shop별 가격을 찾기
3. findPrices3() : CompletableFuture()으로 각 Shop별 가격을 찾기

**[Main 클래스](../../main/java/part16/Example2/Main.java)**
이에 맞는 메인 메소드 역시 만들어준다.  
이때, 각 메서드의 실행 결과는 다음과 같다.  
1. findPrices() : 5057 msec
2. findPrices2() : 1008 msec
3. findPrices3() : 1009 msec
> 각 메서드별 비교는 executor()라는 메소드를 따로 만들어서 진행.  

### 두 번째 예시
사실 parallelStream과 CompletableFuture의 차이가 별로 없다.  
* 그렇다면, 왜 우리는 CompletableFuture를 쓰는 걸까?

**[BestPriceFinder 클래스](../../main/java/part16/Example2/BestPriceFinder.java)**
상점의 갯수를 9개로 늘려보고, 각 Shop별 가격을 나열하는 메서드를 만들면 된다.
1. findPrices() : stream()으로 각 Shop별 가격을 찾기
2. findPrices2() : parallelStream()으로 각 Shop별 가격을 찾기
3. findPrices3() : CompletableFuture()으로 각 Shop별 가격을 찾기
4. findPrices4() : CompletableFuture() + Executor로 각 Shop별 가격을 찾기
- Executor를 이용해서 쓰레드 갯수를 조절 가능.

**[Main2 클래스](../../main/java/part16/Example2/Main2.java)**
9가지의 Shop을 정의하고, 또한 이에 맞는 메인 메소드 역시 만들어준다.

이때, 각 메서드의 실행 결과는 다음과 같다.
1. findPrices() : 9065 msec
2. findPrices2() : 2008 msec
3. findPrices3() : 2009 msec
4. findPrices4() : 1007 msec

왜? 우리가 하는 작업 = 가격 조회 = I/O 작업
* parallelStream은 컬렉션을 CPU 코어 수만큼 나눠서 계산 
* 즉, 연산이 CPU-bound일 때 최고 효율
* 그런데 우리의 작업은 I/O bound.
* 따라서 CompletableFuture + Executor가 더 빠르다.

**즉 parallelStream은 CPU가 많이 필요한 곳에 어울림.  
또한 CompletableFuture는 I/O가 많은 작업에 어울림.**

## 예제코드 3 : 작업 파이프라인 만들어보기
> 이번에는 하나의 작업이 아닌, 여러 개의 작업이 조합되는 경우를 생각해보자.

단순히 가격을 조회하는 것에 더해 할인 등 기능을 넣어보자.  
1. Shop 클래스의 내용도 바꾸고, Util도 추가한다.  
**[Shop 클래스](../../main/java/part16/Example3/Shop.java)**
**[Util 클래스](../../main/java/part16/Example3/Util.java)**
- Shop의 getPrice()의 경우, 다시 동기로 바뀌였는데 이는 일부러 느리게 다시 만들어 놓은 것.
- 여러 단계의 원격 호출이 있는 동기 파이프라인를 만들어, 비동기 구조의 필요성을 체감시키는 것이 목표

2. Discount와 Quote 클래스를 추가해보자.  
**[Discount 클래스](../../main/java/part16/Example3/Discount.java)**
**[Quote 클래스](../../main/java/part16/Example3/Quote.java)**

3. BestPriceFinder를 수정한다. 
* findPriceWithService1 : 단순 stream()으로 이뤄진 
* findPriceWithService2 : completablefuture() + executor
* findPriceWithService3 : 가격 찾기 + 환전을 future()만 가지고 구현
```text
findPriceWithService3의 구현과정
1. 쓰레드풀 구현 : `ExecutorService executor = Executors.newCachedThreadPool();`
newCachedThreadPool()은 I/O 성격에 적합.

2. 첫 번째 작업인 futureRate 구현. 환율을 가지고 옴.
3. 두 번째 작업인 futurePriceInUSD. 가지고 온 환율을 이용해 EUR

```




**여기서 잠깐**
>> completablefuture의 여러 메서드를 한번 정리하고 넘어가자

* thenapply() : 하나의 CompletableFuture<T>가 끝났을 때 그 결과 T를 받아서 **같은 쓰레드에서** U로 변환하는 함수 적용
* thenApplyAsync() : 하나의 CompletableFuture<T>가 끝났을 때 그 결과 T를 받아서 **다른 쓰레드에서** U로 변환하는 함수 적용
```text
CompletableFuture<T>
    .thenApply(Function<T, U>)
```
* thencombine() : 두 작업이 모두 완료되면 두 결과를 받아서 합침 (병렬적)

