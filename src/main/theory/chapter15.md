# CompletableFuture와 리액티브 프로그래밍의 기초
## 자바에서의 동시성
### 왜 '동시성'인가?
1. 멀티코어 cpu의 등장
2. 대규모 인터넷 기반 앱의 등장

### 자바에선, 어떻게 '동시성'을 구현하는가?
#### 1. 멀티프로세싱 
- 프로세스 = 독립된 메모리 공간. IPC가 필요하고, 컨텍스트 스위칭 비용이 큼
- JVM 자체가 이미 프로세스라, 프로세스를 여러 개 굴리는 건 너무 무겁다.
- 따라서 자바에서는 주류가 되지 못함.

#### 2. 멀티쓰레딩 
- 하나의 프로세스 안에서 메모리 공유
- IPC 필요 없으나, 다음과 같은 문제가 있음.
  1) race condition
  2) deadlock
  3) starvation
- 또 여러가지 문제가 있으니...우선 발전 과정에 따라 하나하나 정리해보자!

#### 3. 멀티쓰레딩 (1) : Runnable / Thread 클래스
* 자바 1.0 버전에서 나옴.
* Runnable 클래스 : 쓰레드가 할 일을 정한다.
* Thread 클래스 : 실제 쓰레드
- 인스턴스화를 할 때, Runnable 타입의 객체를 파라미터로 담아 쓰레드로 만든다.
- 이렇게 분리를 하면 쓰레드 재사용, 실행 방식 조정, 상속 문제가 모두 해결되기 때문!

* 예시코드 : 외부 인터넷 주소에서 값을 받아, completed=true 인 갯수를 계산!
  * 최종결과 : 성공 요청 수 / completed=true 개수 / 전체 처리 시간
  * [계산 로직](../../main/java/part15/section1/ResultCounter.java)
  * [쓰레드 동작](../../main/java/part15/section1/ApiTask.java)
  * [Main문](../../main/java/part15/section1/Main.java)

* 문제점 :
  1. Thread를 직접 생성함 -> 비용이 커짐.
  2. 쓰레드 생명주기를 개발자가 직접 관리 -> 제어 포인트가 코드 전체에 흩어짐
  3. 예외 처리 지옥

#### 3. 멀티쓰레딩 (2) : Callable / ExecutorService
* Callable / ExecutorService 모두 자바 5에서 등장
* 