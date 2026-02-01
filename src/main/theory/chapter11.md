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

### Optional 스트림 조작
```text

```
* 값이 없을 수 있음
* 