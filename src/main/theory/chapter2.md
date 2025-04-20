# 2. 동적 파라미터화 코드 전달하기
## 동적 파라미터화란?
* 동작을 파라미터로 전달하는 것.
* 즉 과거에는 정적인 변수만을 파라미터로 전달했다면, 모던 자바에서는 동작(=함수)를 전달할 수 있다는 것

**이걸 왜 해야하는뎨?**  
* 다양한 상황에 좀 더 효과적으로 대응할 수 있기 때문에!

**예시 : 사과의 구분**  
1. `firstfilter()` : '초록색'이라는 정적인 값만 구분 가능.
```java
public static List<Apple> firstfilter(List<Apple> apples) {
    List<Apple> result = new ArrayList<>();
    for (Apple apple : apples) {
        if (apple.getColor() == Color.GREEN) {
            result.add(apple);
        }
    }
    return result;
}
```
문제 : 조건이 늘어나면 if문을 계속 넣어야 한다.


`secondfilter()` : '색' 자체를 파라미터로 넣어 좀 더 유연성 추가  
```java
public static List<Apple> secondfilter(List<Apple> apples, Color color) {
    List<Apple> result = new ArrayList<>();
    for (Apple apple : apples) {
        if (apple.getColor() == color) {
            result.add(apple);
        }
    }
    return result;
}
```

아까보다는 좀 더 유연해졌지만, 만약 '무게'라는 조건이 포함되면 그때는 또 if문을 추가해야 한다.

3. `thirdfilter()` : 그럼 '조건'을 파라미터로 넣어보자!
```java
interface ApplePredicate {
    boolean test(Apple apple);
}

class AppleGreenAndHeavyPredicate implements ApplePredicate {
    public boolean test(Apple apple) {
        return apple.getColor() == Color.GREEN && apple.getWeight() > 150;
    }
}
```

ApplePredicate 클래스를 이용해 '조건' 그 자체를 파라미터로 만들었다.  

```java
public static List<Apple> thirdfilter(List<Apple> apples, ApplePredicate p) {
    List<Apple> result = new ArrayList<>();
    for (Apple apple : apples) {
        if (p.test(apple)) {
            result.add(apple);
        }
    }
    return result;
}
```

이 경우 다음과 같이 좀 더 코드가 간결해졌다. 
```java
List<Apple> result = thirdfilter(apples, new AppleGreenAndHeavyPredicate());
```
그렇지만 이러한 방식을 좀 더 세련되게 쓰고 싶다면?


4. fourthfilter()` : 람다식 사용
```java
public static <T> List<T> fourthfilter(List<T> list, Predicate<T> p) {
    List<T> result = new ArrayList<>();
    for (T item : list) {
        if (p.test(item)) {
            result.add(item);
        }
    }
    return result;
}
```

이 경우 다음과 같이 좀 더 코드가 간결해졌다.
```java
List<Apple> result = fourthfilter(apples, (Apple apple) -> apple.getColor().equals(Color.RED));
```

## 람다식의 사용예시
* 람다식을 이용하면 좀 더 편리하게 코드를 작성할 수 있다.
* 또한, 병렬처리가 필요한 코드에도 효과적이다.

```java
public class RunnableExample {
    public static void main(String[] args) {
        Runnable task = () -> System.out.println("🧵 Runnable 실행 (람다식)");

        Thread thread = new Thread(task);
        thread.start();
    }
}
```