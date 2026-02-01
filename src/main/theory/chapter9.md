# 리팩터링, 테스팅, 디버깅
## 1. 가독성 & 유연성을 개선하는 리팩터링
* 익명 클래스 -> 람다 표현식
* 람다 표현식 -> 메서드 참조
* 명령형 데이터 처리 -> 스트림

### 익명 클래스를 람다 표현식으로 리팩터링

* Before (명령형)
```text
Runnable r1 = new Runnable() {
    public void run() {
        System.out.println("");
    }
}
```

* After (함수형)
```text
Runnable r = () -> System.out.println("Hello");
```    

### 람다 표현식을 메서드 참조로 리팩터링

* Before
```text
inventory.sort((a, b) -> a.getWeight().compareTo(b.getWeight()));
```

* After
```text
inventory.sort(comparing(Apple::getWeight));
```

* 장점 
  * 의도(Intent)가 더 명확
  * 테스트·재사용 용이 
  * 이름이 곧 문서 역할

### 명령형 데이터 처리를 스트림으로 리팩터링
* Before
```text
List<String> dishNames = new ArrayList<>();
for (Dish dish : menu) {
    if (dish.getCalories() > 300) {
        dishNames.add(dish.getName());
    }
}
```

* After
```text
List<String> dishNames =
    menu.stream()
        .filter(d -> d.getCalories() > 300)
        .map(Dish::getName)
        .toList();
```

## 2. 람다로 객체지향 디자인 패턴 리팩터링하기
> 람다는 전통적인 객체지향 디자인 패턴을 더 간결하고 명확하게 표현하게 해줌
> 이 장에서는 5가지 패턴을 어떻게 람다로 재구성할지를 보여준다.

### 전략 패턴
> 전략 패턴이란?
> 알고리즘(전략)을 캡슐화하여, 실행 중에 전략을 교체할 수 있도록 하는 패턴


* 언제 쓰이나? 
- 검증 로직
- 정렬 기준
- 할인 정책
- 조건별 처리 로직

* [예시코드](../java/part9/StrategyMain.java)
* 람다식을 사용하면 굳이 IsNumeric, IsAllLowerCase 등을 만들 필요가 없어짐.

### 템플릿 메서드 패턴


