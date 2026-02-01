# null 대신 Optional 클래스
## Optional 클래스란?
```text
Optional<T>
```
* Null이 있을수도 있음을 확인하고 쓰는 안전장치!
* T 타입의 값이 있을수도, 없을수도 있다!

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

```text
public String getCarInsuranceName(Person person) {
    return person.getCar().getInsurance().getName();
}
``` 

### flatMap으로 Optional 객체 연결
