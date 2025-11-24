# 5. 스트림 활용
## 필터링
### 1) filter(Predicate 조건)
* 스트림 요소들 중 특정 조건을 만족하는 요소만 선택하는 연산.
```java
stream.filter(x -> x > 10)
```
* filter는 중간 연산 (lazy)
* Predicate<T>를 함수로 받음 (true면 남기고, false면 제거)
* 원본 리스트는 변경하지 않음 (불변)

### 2) distinct() — 중복 제거
* 스트림에서 중복된 요소를 제거할 수 있음.
```java
stream.distinct()
```
* equals() & hashCode() 기준으로 비교
* 중간 연산(lazy)

### 연습문제
“칼로리가 500 미만인 채식 요리(vegetarian)만 골라서, 이름을 중복 제거한 리스트를 스트림으로 만들어라.”
* [Dish 클래스](../java/part5/prac1/Dish.java)
* [해답](../java/part5/prac1/FindVegetarian.java)

## 슬라이싱
### 1) takeWhile(predicate) — 조건이 참인 동안만 앞에서 자르기
* 정렬된(stream sorted) 리스트에서 특히 강력함.
* 동작 방식 : 앞에서부터 조건이 false가 되는 첫 순간까지 가져오기.
  * 조건이 false 되는 순간 바로 종료 → 매우 빠름
```java
list.stream().takeWhile(x -> x < 10)
```

### 2) dropWhile(predicate) — 조건이 참인 동안 버리고 남은 것만 가져오기
* 예: 1,2,3,10,11,12 에 dropWhile(x<10) → 10,11,12
```java
list.stream().dropWhile(x -> x < 10)
```

### 3) limit(n) — 앞에서 n개만 가져오기
```java
stream.limit(3)
```

### 4) skip(n) — 앞에서 n개 버리고 나머지 가져오기
```java
stream.skip(2)
```

### 연습문제
* 아래와 같이 Dish 클래스가 있고, 해당 클래스의 요리(dish)는 칼로리 오름차순으로 정렬된 상태라고 가정하자.
  * [Dish 클래스](../java/part5/prac1/Dish.java)
* 칼로리가 500 미만인 요리만 “앞에서부터” takeWhile로 추출한 뒤, 그 중 처음 2개만(limit) 골라서 이름 리스트로 출력하라.
* [해답](../java/part5/prac1/FindFirstTwo.java)

## 매핑
### 1) map — 요소를 1:1로 변환
```java
List<String> names = menu.stream()
        .map(Dish::getName)
        .collect(toList());
```

### 2) mapToInt / mapToLong / mapToDouble
* 기본형 특화 스트림(IntStream 등)으로 변환하는 버전.
* 오토박싱/언박싱 비용 없음 → 성능 좋음
* 합계(sum), 평균(average) 등 기본형 스트림만 가능한 연산 사용 가능

```java
int totalCalories = menu.stream()
        .mapToInt(Dish::getCalories)
        .sum();
```

### 3) flatMap — 중첩된 리스트를 “평탄화”하여 하나의 스트림으로
* 리스트의 “리스트” → 요소 하나들의 스트림으로 펼치는 것.
```java
List<List<Integer>> list = List.of(
    List.of(1,2,3),
    List.of(4,5)
);
```

### 연습문제
* 다음과 같은 리스트가 있다. 
```java
List<String> words = Arrays.asList("Hello", "World");
```
* 각 단어를 구성하는 문자(Character) 하나씩을 모두 출력하는 스트림을 flatMap을 이용해 만들어라.
* [해답](../java/part5/prac3/PrintChar.java)

## 고급 매핑
### 1) flatMap으로 모든 조합(Cartesian Product) 만들기
* 두 리스트가 있을 때:
```java
List<Integer> numbers1 = Arrays.asList(1,2,3);
List<Integer> numbers2 = Arrays.asList(3,4);
```
* 이 둘의 **모든 가능한 조합(쌍)**을 만들고 싶다면? 
  * map만 쓰면 중첩된 스트림이 생김
```java
numbers1.stream()
        .map(i -> numbers2.stream().map(j -> new int[]{i, j}))
```
  * Stream<Stream<int[]>> 가 되어버림. 내가 원하는 건 모든 (i,j) 쌍의 스트림 하나!
* 그래서 flatMap으로 중첩 제거가 필요함.

### 2) flatMap + filter 조합 (실무에서 가장 많이 쓰는 패턴)
* 예를 들어, 두 숫자 조합 중 i + j가 짝수인 조합만 고르고 싶다면:
```java
numbers1.stream()
    .flatMap(i -> numbers2.stream()
        .filter(j -> (i + j) % 2 == 0)
        .map(j -> new int[]{i, j})
    )
```
  * flatMap으로 조합 생성 
  * filter로 조건 적용	
  * map으로 pair 생성
* 이렇게 “flatMap → (필요 시 filter) → map” 형태가 실무 스트림 파이프라인의 정석 패턴이다.

### 연습 문제 1
* 다음 단어 목록이 있다 :
```java
List<String> words1 = Arrays.asList("java", "spring", "cloud");
List<String> words2 = Arrays.asList("boot", "core", "ai");
```
* 다음 조건을 모두 만족하는 **단어 쌍 (w1, w2)**를 만들어라.
  1. 모든 가능한 단어 조합을 만든다. 
  2. 각 쌍의 길이 합을 구한다.
  3. 길이 합이 10 이상인 단어 조합만 남긴다.
  4. 출력은 다음과 같은 리스트 형식이어야 한다:
* 출력 예시
```java
[java-boot(8), spring-core(??), ...]
```
* [해답](../java/part5/prac4/Example1.java)

### 연습 문제 2
다음 리스트가 있다:
```java
List<Integer> A = Arrays.asList(1, 2, 3, 4);
List<Integer> B = Arrays.asList(5, 6, 7);
```
* 요구조건
  1. 두 리스트 A와 B의 모든 가능한 조합 (a, b)를 만든다
  2. 두 수의 차이(|a - b|) 가 홀수인 경우만 남겨라
  3. 조건을 만족하는 조합을 다음 문자열 포맷으로 변환하라:
* [해답](../java/part5/prac4/Example2.java)

## 검색과 매칭
### 1) anyMatch(predicate)
* 하나라도 조건을 만족하면 true 
* 하나 찾으면 즉시 종료됨 → 매우 빠름.

### 2) allMatch(predicate)
* 모든 요소가 조건을 만족하면 true
* 하나라도 false 나오면 즉시 종료.

### 3) noneMatch(predicate)
* 모든 요소가 조건을 만족하지 않을 때 true
* 즉.. !anyMatch와 같다!

### 4) findAny()
* 조건을 만족하는 임의의 요소 하나를 Optional로 반환 
* 병렬 스트림(parallel)에서 안정적

### 5) findFirst()
* 조건을 만족하는 첫 번째 요소를 Optional로

### 연습 문제
* 다음 Dish 리스트가 있다고 하자:
```java
List<Dish> menu = Arrays.asList(
    new Dish("pork", false, 800),
    new Dish("beef", false, 700),
    new Dish("chicken", false, 400),
    new Dish("salad", true, 150),
    new Dish("rice", true, 350),
    new Dish("fish", false, 450)
);
```
1. 칼로리가 600 이상인 요리가 하나라도 있는지 anyMatch로 검사하라.
2. 모든 요리의 칼로리가 900 미만인지 allMatch로 확인하라.
3. 칼로리가 1000 이상인 요리가 하나도 없는지 noneMatch로 확인하라.
4. 채식 요리(vegetarian=true) 중 “칼로리가 가장 낮은 요리”를 findFirst로 찾아라.
5. 칼로리가 500 이하인 요리 하나를 findAny()로 반환하라.

* [해답](../java/part5/prac5/Example1.java)

## 리듀싱
### 리듀싱이란?
* 스트림의 모든 요소를 하나의 결과값으로 합치는 것(집계)
* 예: 합계, 곱, 최대값, 최소값, 문자열 결합 등

* map과의 차이
  * map : 결과가 “여전히 리스트/스트림”이다. 중간연산임.
  * reduce : int, String 등 하나의 값을 만들 수 있음. 최종연산임.

* reduce에는 3가지 버전이 있음.
  * reduce(accumulator)
  * reduce(identity, accumulator)
  * reduce(identity, accumulator, combiner)
* identity : 초기값
* accumulator : 계산방식
* combiner : 결합방식

### 1) reduce(accumulator)
```java
Optional<Integer> sum = numbers.stream()
        .reduce((a, b) -> a + b);
```

### 2) reduce(identity, accumulator)
```java
int sum = numbers.stream()
        .reduce(0, (a, b) -> a + b);
```

### 3) reduce(identity, accumulator, combiner)
```java
// numbermap의 타입이 HashMap이라고 해보자.
int sum = numbermap.stream()
        .reduce(0, (sum, numbermap) -> sum + numbermap.getnumber(), Integer::sum);
```
* 이 버전은 ...
  * ✔ **병렬 스트림(parallel stream)**을 위해 존재한다.
  * ✔ 요소 타입(T)와 결과 타입(U)을 다르게 만들기 위해 존재한다. 
  * ✔ 성능 때문에 필요하다.
* 자세한 건 7장에서!

### max/min : reduce로!
```java
Optional<Integer> max = numbers.stream()
        .reduce(Integer::max);

Optional<Integer> min = numbers.stream()
        .reduce(Integer::min);
```

### 4) 문자열 이어 붙이기 : reduce로!
```java
String joined = words.stream()
        .reduce("", (a, b) -> a + b);
```

### 연습문제 1
1. reduce(accumulator)만 사용해서 최대값 찾기 (Optional 버전)
```java
List<Integer> nums = Arrays.asList(3, 7, 2, 11, 9, 5);
```
* 다음 조건을 만족하는 코드를 작성하라:
  * reduce(accumulator) 하나만 사용한다 
  * identity(초기값)는 쓰면 안 된다 
  * 결과 리턴 타입은 Optional<Integer>
  * 리스트 내 가장 큰 값을 찾아서 출력하라

2. reduce(identity, accumulator)만 사용해서 문자열 길이 합계 구하기
```java
List<String> words = Arrays.asList("java", "stream", "lambda", "reduce");
```
* 다음 조건을 만족하는 코드를 작성하라:
  * reduce(identity, accumulator) 2-arg 버전만 사용 
  * Optional 없이 직접 int 결과를 받는다 
  * 문자열 전체 길이의 합계를 구하라

### 연습문제 2
1. reduce만 사용해서 칼로리 총합 구하기
2. reduce로 가장 칼로리 높은 Dish 찾기
3. reduce로 모든 요리 이름 이어 붙이기
* [해답](../java/part5/prac6/Example2.java)

## 기본형 스트림
* Integer, Long, Double은 모두 객체라서 오토박싱/언박싱 비용 발생 
* 예를 들어 다음과 같은 상황에서 비용이 발생한다.
```java
Stream<Integer> → int
```
* 그래서 등장한 것이 기본형 스트림 3종류 : IntStream, LongStream, DoubleStream
  * 오토박싱/언박싱 비용 제거
  * sum(), count(), average() 등 빠른 연산 제공
  * OptionalInt, OptionalLong 같은 primitive Optional 제공

### 객체 스트림 -> 기본형 스트림
* `mapToInt()`, `mapToDouble()`, `mapToLong()`
* 객체 스트림을 기본형 스트림으로 변환하는 메서드들.
```java
int calories = menu.stream()
        .mapToInt(Dish::getCalories)
        .sum();
```

### 기본형 스트림 -> 객체 스트림
* `boxed()` 라는 메소드를 사용하면 됨.
```java
IntStream.range(1,5)
    .boxed() 
    .collect(toList());   // Stream<Integer> 로 변환됨
```

### IntStream : 대표 연산들
* sum(), max(), average(), range(), rangeClosed()
```java
OptionalInt max = IntStream.of(1,4,2).max();
OptionalInt max = IntStream.of(1,4,2).max();
OptionalDouble avg = IntStream.of(1,4,2).average();
IntStream.range(1, 5);      // 1,2,3,4
IntStream.rangeClosed(1, 5); // 1,2,3,4,5
```

### OptionalInt / OptionalDouble / OptionalLong
* 기본형 스트림 전용 Optional 타입.
```java
OptionalInt max = IntStream.of().max();
System.out.println(max.orElse(0)); // 값 없으면 0 사용
```

### 연습문제
1. IntStream으로 칼로리 총합 구하기 (dish 리스트 이용)
* [해답]()
2. 

3. 