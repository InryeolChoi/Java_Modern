# 3. 람다식 써보기!
## 복습
* 함수를 파라미터로 주면, 더 간결하게 코드를 칠 수 있다.  
* 그러나 함수를 마구잡이로 넣으면 불편하기에, 람다식이라는 것을 도입했다.  
* 그럼 정확히 람다식이 뭘까?  

## 람다 표현식
* 메서드로 전달할 수 있는 익명함수를 단순화 한 것
* 어떤 메서드를 단순히 표현하는 문법적 표현이라고 생각하면 된다.

<특징>
* 익명성 : 이름 없는 함수 
* 간결함 : 익명 내부 클래스보다 코드가 훨씬 더 짧아짐.
  * 익명 클래스 : 선언과 객체화를 한번에 하는 클래스.
* 함수형 인터페이스 필요 : 단 하나의 추상 메서드를 가진 인터페이스만 사용(@FunctionalInterface)
* 지연 실행 가능 : 실제로 사용될 때 사용됨

<비교하기>  
(1) 람다 미사용
```java
Comparator<Apple> byWeight = new Comparator<Apple>() {
    public int compare(Apple a1, Apple a2) {
        return a1.getWeight().compareTo(a2.getWeight());
    }
}
```
(2) 람다 사용
```java
Comparator<Apple> byweight = (Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight());
```
-> 훨씬 더 코드가 간결해진 것을 확인할 수 있음.

<람다식 예제>
```txt
(String s) -> s.length();
(Apple a) -> a.getWeight() > 150;
(int x, int y) -> {
    System.out.println(x + y);
}
() -> 42
```

## 람다식 사용예시
### 1. 함수형 인터페이스
* 함수형 인터페이스란? 오직 하나의 추상 메서드만 가진 인터페이스
```text
@FunctionalInterface
interface MyFunction {
    void run(); // 추상 메서드 1개만!
}
// @FunctionalInterface는 컴파일러가 이 규칙을 강제하게 만듬.
```
* 람다는 “함수(=동작)”를 값처럼 전달하기 위한 문법
* 그런데 자바는 객체지향 언어라서 “함수만”을 넘길 수는 없음.
* 즉, 메서드를 가진 객체(즉, 인터페이스의 구현체)”를 넘겨야 함.
* 그래서 람다식이 곧 함수형 인터페이스의 익명 구현 객체!

이를 자바스크립트와 비교해보면...
<javascript>
```javascript
function greet() {
  console.log("안녕!");
}

function doTwice(action) {
  action();
  action();
}

doTwice(greet);
doTwice(() => console.log("안녕!"));
```
```java
public class LambdaExample {
    // 1️⃣ 함수형 인터페이스 정의 (매개변수도 반환값도 없는 함수)
    @FunctionalInterface
    interface Action {
        void run();
    }

    // 2️⃣ action을 두 번 실행하는 메서드
    static void doTwice(Action action) {
        action.run();
        action.run();
    }

    // 3️⃣ main 메서드
    public static void main(String[] args) {
        // 람다식으로 전달
        doTwice(() -> System.out.println("안녕!"));
    }
}
```

### 2. 함수 디스크립터
* 함수 디스크립터란? 함수형 인터페이스 속 추상 메서드의 입출력!
```java
@FunctionalInterface
interface Calculator {
    int calculate(int x, int y);
}
```
➡️ 이 인터페이스의 추상 메서드 calculate(int, int)  
➡️ 함수 디스크립터는 (int, int) -> int  
➡️ 람다는 “이 함수 디스크립터를 따르는 함수 구현체”  

### 3. 실행 어라운드 패턴
* 실행 어라운드 패턴 : 준비 코드와 정리 코드가 작업 코드를 감싸고 있는 형태.
* 이를 람다식으로 표현가능함. 
1. 먼저 다음과 같이 함수형 인터페이스를 선언함.
```java
import java.io.BufferedReader;
import java.io.IOException;

@FunctionalInterface
public interface BufferedReaderProcessor {
    String process(BufferedReader br) throws IOException;
}
```

2. 이를 이용한 람다식을 만들어 냄
```java
public class ExecuteAroundExample {
    public static void main(String[] args) {
        try {
            String oneLine = processFile((BufferedReader br) -> br.readLine());
            System.out.println("oneLine의 결과 : " + oneLine);

            String twoLine = processFile((BufferedReader br) -> br.readLine() + br.readLine());
            System.out.println("twoLine의 결과 : " + twoLine);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String processFile(BufferedReaderProcessor p) throws IOException {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(part3.prac1.ExecuteAroundExample.class.getResourceAsStream("/data.txt"))
        )) {return p.process(br);}
    }
}
```

### 4. 함수형 인터페이스 - 예시
(1) Predicate  
* 조건을 표현하는 함수형 인터페이스  
* 메서드 시그니처는 `boolean test(T t)`

* (2) Consumer  
* 입력은 받고, 반환값은 없는” 함수를 표현하는 함수형 인터페이스  
* T 타입의 값을 받아서 어떤 동작을 수행, return 값이 없음.  
* 메서드 시그니처는 `void accept(T t);`

(3) Function
* T 타입을 받아서 R 타입을 리턴하는 함수형 인터페이스.  
* 메서드 시그니처는 `R apply(T t)`

(4) 오토박싱 피하는 용도로 쓰는 함수형 인터페이스
* 박싱 : 기본형 타입 -> 참조형 타입 
* 언박싱 : 참조형 타입 -> 기본형 타입
* 일반적인 함수형 인터페이스는 박싱을 작동으로 하는 오토박싱이 적용됨. 성능의 손해가 발생
* 그래서 일부 함수형 인터페이스는 이걸 막기 위해 기본형 전용으로 설계됨.
* ex. IntPredicate, LongPredicate 등등...

## 타입검사, 타입추론, 제약
### 1. 람다 표현식에서 타입 추론(Type Inference)
* 자바는 람다식의 파라미터 타입을 대부분의 경우 생략할 수 있다.
* 왜냐하면 람다식이 할당되는 위치(컨텍스트)를 기반으로 컴파일러가 타입을 추론하기 때문이다.

```java
List<String> list = Arrays.asList("a", "b", "c");
list.sort((s1, s2) -> s1.compareToIgnoreCase(s2));
```

* 핵심 개념 1 — 람다의 “대상 타입(Target Type)”
  * 람다의 타입을 결정하는 것은 대상 타입이다.
  * 즉, 람다 표현식이 사용된 위치에서 요구하는 함수형 인터페이스가 람다의 타입을 결정한다.
```java
Predicate<String> p = s -> s.length() > 0;
```
여기서 s는 자동으로 String 타입으로 추론됨.

* 핵심 개념 2 — 자바 컴파일러는 문맥 기반으로 타입을 결정한다
  * 컴파일러는 다음과 같은 정보를 조합해 타입을 추론한다:
    * 람다가 사용되는 메서드의 파라미터 타입 
    * 람다가 할당되는 변수 타입 
    * 제네릭 메서드라면 인자 타입 
    * 오버로드된 메서드라면 가장 구체적인 시그니처 선택

* ❗중요한 제약 — 람다의 파라미터는 ‘일관성 있는 형식’을 가져야 한다
  * 람다의 파라미터를 선언할 때, 모두 타입을 쓰거나 모두 타입을 생략해야 한다.
  * 섞어서 쓰면 안 됨.

* 핵심 개념 3 — 람다에서 지역 변수 캡처 규칙 
  * 람다식 내부에서 바깥 지역 변수를 사용할 수 있다. 하지만 반드시 final 또는 effectively final(사실상 final) 이어야 한다. 
  * 왜? 람다는 멀티스레드 환경에서도 안전하게 동작해야 하기 때문이고, 지역 변수가 변경 가능하면 일관성이 깨질 수 있기 때문에 금지된다.

```java
int port = 8080;
Runnable r = () -> System.out.println(port);

// port = 9090; // ← 변경하면 컴파일 오류 발생!
```

## 메서드 참조
* 람다 표현식을 더 간결하고 읽기 쉽게 만드는 문법.
* 람다로 이미 존재하는 메서드 하나만 호출하는 형태라면 → 메서드 참조로 대체 가능!

* 람다 vs 메서드 참조 비교:
```java
(words) -> Integer.parseInt(words)   // 람다
Integer::parseInt                     // 메서드 참조
```

### 메서드 참조의 3가지 유형
1. 정적 메서드 참조
```java
Function<String, Integer> f = Integer::parseInt;
```

2. 특정 객체의 인스턴스 메서드 참조
```java
// 메서드 참조
PrintStream out = System.out;
Consumer<String> c = out::println;

// 람다
Consumer<String> c = x -> out.println(x);
```

3. 임의 객체의 인스턴스 메서드 참조
```java
String[] arr = {"a", "b", "c"};
Arrays.sort(arr, String::compareToIgnoreCase);
```

### 생성자 참조
* 생성자도 메서드처럼 참조할 수 있다.

```java
Supplier<ArrayList<String>> s = () -> new ArrayList<>();
```
