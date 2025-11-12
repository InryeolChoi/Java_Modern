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

### 4. 함수형 인터페이스 사용