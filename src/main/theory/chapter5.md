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
