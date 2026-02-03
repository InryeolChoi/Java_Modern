# null 대신 Optional 클래스
## Optional 클래스란?
```text
Optional<T>
```
* Null이 있을수도 있음을 확인하고 쓰는 안전장치!
* T 타입의 값이 있을수도, 없을수도 있다!
* **단, 클래스의 변수 선언(필드)용으로는 쓸 수 없음!**

**만드는 방법**
```text
Optional<String> o1 = Optional.of("hello");     // null ❌
Optional<String> o2 = Optional.ofNullable(str); // null 가능
Optional<String> o3 = Optional.empty();         // 빈 Optional
```

## Optional 적용 패턴
### Optional 객체 만들기
```text
// 빈 Optional 객체
Optional<Car> OptCar = Optional.empty();

// 값을 넣을때
Optional<Car> OptCar = Optional.of(car);

// null 값 만들기
Optional<Car> optCar = Optional.ofNullable(car);
```
* `.ofNullable()`을 쓰면 car 변수의 값에 따라 결과가 달라짐 
  * car가 null이 아니면 → Car 객체를 담은 Optional 생성 
  * car가 null이면 → 빈 Optional 생성

### Map으로 Optional 값을 추출 및 변환
```text
Optional<Insurance> optIns = Optional.ofNullable(insurance);
Optional<String> name = OptIns.map(Insurance::getName);
```

### flatMap으로 Optional 객체 연결
* 맵을 쓰다보면, 여러개의 Optional이 겹쳐있는 경우가 있다.
```text
Optional<Car> car = Optional.of(
    new Car(Optional.of(new Insurance("AXA")))
);

??? result =
    car.map(Car::getInsurance);
```

* 여기서 ???의 결과는 `Optional<Optional<Insurance>>`이다.
* 쓰기도 불편하고, 활용도 잘 못할 거 같은 걸 방지하기 위해 `flatmap()`을 사용함.
* 만약 값이 NULL인 경우, `orElse()`를 통해 기본값을 부여할 수 있음.
```text
Optional<String> name = person
        .flatMap(Person::getCar)
        .flatMap(Car::getInsurance)
        .map(Insurance::getName)
        .orElse("unknown");
```
* 예제코드는 다음 [링크](../java/part11/OptionalExample.java)를 참조.

### Optional 스트림 조작
> Optional이 섞인 컬렉션을 Stream으로 “자연스럽게” 처리해보자.

* 예제코드는 다음 [링크](../java/part11/OptionalExample2.java)를 참조.
* `Optional::stream` : Optional 객체를 0개 또는 1개의 요소를 가지는 Stream으로 변환
* 이를 `flatmap()`과 같이 쓰면 Stream 안에 섞여 있는 Optional을 “없애고 값만 남긴다” 는 것

### 디폴트 액션과 Optional 언랩
> 어떻게 하면 Optional을 벗길 수 있을까?

| 상황                      | 선택             | 
|-------------------------|----------------|
| 기본값이 필요                 | orElse         |
| 기본값이 필요 + 기본값으로 람다 실행 시 | orElseGet      |
| 없으면 오류                  |  orElseThrow   |
| 값이 있을 때만 행동             | ifPresent      |
| 있/없 둘 다 처리              | ifPresentOrElse |

* 예제코드는 다음 [링크](../java/part11/OptionalExample3.java)를 참조.

### 두 Optional 합치기
* 두 값이 “모두 있을 때만” 계산하고 싶으면 flatMap + map을 쓴다.
* ex. a와 b를 합치고 싶다면?
```text
Optional<Integer> a = Optional.of(10);
Optional<Integer> b = Optional.of(20);

Optional<Integer> sum = 
    a.flatmap(x ->
        b.map(y -> x + y)
    );
```

* Optional 합치는 전용 메서드는 없음. 
  * Optional은 컬렉션이 아님
* 따라서 복잡한 조합은 Stream으로 가야 함.

### 필터로 특정값 거르기
> 조건을 만족하지 않으면 Optional을 비워버린다
```text
Optional<T>.filter(Predicate<? super T>)
```

* 값이 있고 조건을 만족하면 → 그대로 유지 
* 불만족하면 → Optional.empty()
* ex. 홀짝 구분 예제
```text
Optional<Integer> opt = Optional.of(10);

Optional<Integer> even = opt.filter(n -> n % 2 == 0);
```

## Optional을 올바르게 사용하는 방법
* null이 될 수 있는 대상을 Optional로 감싸기 
  * null을 숨기기 위해 
  * 값의 부재 가능성을 API에 드러내기 위해서 ✅

* 예외와 Optional 클래스
  * 
  * 

* 기본형 Optional을 사용하지 말아야 하는 이유
  * 
  * 

* 응용
  * 
