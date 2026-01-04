# 4. 스트림 소개
## 스트림이란?
* 정의1) 선언형 코드를 이용해 컬렉션 데이터를 처리할 수 있는 기능
* 정의2) 데이터 처리 연산을 지원하도록 소스에서 추출된 연속된 요소
    * 연속된 요소 : 스트림이 데이터의 흐름임을 의미함. 일종의 파이프라고 생각하자.
    * 소스 : 데이터의 원천. 컬렉션, 배열, 파일, 숫자범위, I/O 자원
    * 데이터 처리 연산 : 스트림이 하는 역할 (필터링, 매핑, 정렬, 합계 등등)

## 스트림의 특징
### 1. 데이터를 병렬로 처리할 수 있음. 
* 컬렉션을 여러 조각으로 나누고, 각 조각을 여러 스레드가 동시에 처리하게 만든 뒤, 마지막에 결과를 합치는 방식
* 개발자가 직접 쓰레드를 만들 필요가 없음.
* [자세한 내용은 7장에서 다룸](./chapter7.md)

### 2. 데이터를 선언적으로 처리함.
* 어떻게 순회할지 코드를 직접 쓰지 않고 “이 데이터를 이렇게 처리해줘”라고 지시만 하는 방식
* ex1) 스트림을 안 쓰면 : "어떻게"를 직접 명시해야 함.
```java
List<String> names = new ArrayList<>();
for (Dish d : menu) {names.add(d.getName());}
```
* ex2) 스트림을 쓰면 : "어떻게"를 명시하지 않고 "무엇을" 할지만 작성함. 
```java
menu.stream()
  .filter(dish -> dish.getCalories() > 300)
  .map(Dish::getName)
  .sorted()
  .collect(toList());
```

### 3. 1번만 쓸 수 있음.
* 스트림은 한 번만 탐색 가능함.
```java
List<String> title = Arrays.asList("Java", "Python", "C++", "Rust");
Stream<String> s = title.stream();
// title의 각 단어를 출력
s.forEach(System.out::println);

// 출력이 불가능함.
s.forEach(System.out::println); 
// 이미 's'라는 스트림으로 title을 흘렸으므로 불가능.
// 이렇게 하고 싶으면 새로운 스트림을 만들어야 함.
```

### 4. 파이프라이닝
* 스트림 연산은 ‘연결해서’ 사용한다는 뜻.
```java
menu.stream()
    .filter(dish -> dish.getCalories() > 300)
    .map(Dish::getName)
    .sorted()
    .collect(toList());
```

### 5. 내부반복 : for 안 써도 알아서 반복을 처리함.
* 왜 내부 반복이 더 좋은가 
  * 반복 로직을 개발자가 제어하지 않으므로, 코드가 훨씬 깔끔해짐.
  * 내부 반복은 “지연 평가(lazy evaluation)“가 가능함.
  * 내부 반복 덕분에 병렬 처리가 쉬워짐. 쓰레드 만들기, lock 걸기를 할 필요가 없음.

* 지연 평가란? 당장 계산 안 하고 기다렸다가, 정말 필요한 순간에만 수행하는 것.
  * 스트림에는 중간연산과 최종연산이라는 개념이 있으며, 최종연산 때 한번에 수행됨.
```java
menu.stream()
    .filter(d -> d.getCalories() > 300) // 여기서는 아직 실행 안 됨!
    .map(Dish::getName)                 // 여기도 실행 안 됨!
    .collect(toList());                 // 여기서 한 번에 실행됨!
```

## 스트림 연산
### 중간연산
* 데이터를 받아서 스트림을 반환하는 연산
* 여러 중간 연산을 합쳐서 질의를 만들 수 있음.
* ex. filter, map, limit, sorted, distinct

### 최종연산
* 결과를 도출하는 연산
* ex. foreach, count, collect

## 예시문제
### 문제 1 — 베스트셀러 필터링
* 우선 오른쪽 [Product 링크](../java/part4/prac2/Product.java)를 참조해 클래스를 만들자.
* 요구사항 
  * 카테고리가 “Electronics”인 상품만 필터링 
  * 그 중에서 평점(rating)이 4.5 이상인 상품만 남기기 
  * 판매량(sales) 기준으로 내림차순 정렬하기 
  * 상위 5개 상품의 이름만 리스트로 추출하기
* [해답1](../java/part4/prac2/FilterProduct.java)

### 문제 2 — 카테고리별 평균 가격 계산
* 요구사항
  * 카테고리(category)별 평균 가격을 Map<String, Double>으로 계산
  * 스트림의 Collectors.groupingBy 와 Collectors.averagingInt 를 활용할 것
* [해답2](../java/part4/prac2/FilterProduct2.java)

### 문제 3 — 
* 우선 오른쪽 [Order 링크](../java/part4/prac2/Order.java)를 참조해 클래스를 만들자.
* 요구사항
  * 쿠팡 로켓배송처럼 “주문일 + 배송일수 = 배송 예정일” 을 계산해야 한다.
  * 남은 주문들에 대해 `배송 예정일 = orderDate.plusDays(deliveryDays)`
  * 결과를 Map<Integer, LocalDate> 형태로 만들기
* [해답2](../java/part4/prac2/FilterProduct3.java)