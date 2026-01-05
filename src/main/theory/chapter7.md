# 7. 병렬 데이터 처리와 성능
## 병렬 스트림이란?
* **병렬 스트림(parallel stream)** 은  
  자바 스트림 API에서 **데이터를 여러 스레드로 나누어 동시에 처리**할 수 있도록 만든 스트림이다.
* 스트림 자체는 병렬이 아님! 선언형으로 생겼을 뿐!

```text
list.parallelStream()
    .filter(x -> x > 10)
    .map(x -> x * 2)
    .sum();
```
* 내부적으로 Fork/Join 프레임워크 사용
* 개발자는 스레드 관리 ❌, 로직만 작성 ⭕ 
* 기본 스레드 수는 CPU 코어 개수 기준 
* 즉 “하나의 큰 작업을 여러 CPU 코어가 나눠서 처리하는 방식”

**<언제 병렬 스트림인가?>** 
1. 처리하고자 하는 데이터의 양이 많고, 처리 방법이 CPU 연산 중심일때
   * CPU 연산 중심의 예시) 계산, 암호화, 영상처리 등등
   * CPU 연산이 중심적이지 않은 경우) 파일 읽기, DB 조회, 네트워크
2. 여러 쓰레드가 같은 변수를 건드리지 않아도 될 때
3. 처리 결과 상 순서가 중요하지 않을 때

## 병렬 스트림의 생성
### 스트림 성능 측정
* 스트림이 얼마나 빠른지 한번 실제로 측정을 해보자!
* 벤치마크 도구로 JMH(Java Microbenchmark Harness), 빌드도구로는 Gradle을 사용
  * 책에서는 Maven을 사용했지만, Gradle로 변경
  * JMH란? OpenJDK 팀이 직접 만든 공식 마이크로벤치마크 도구

* 주의사항?
  * JMH은 src 폴더 밑에 따로 만들어야 함.
  * @BenchMark가 실행 단위
  * Gradle 설정이 중요하므로, 이 부분도 잘 확인하기
  * Gradle 설정 내역은 build.gradle 참조
  * 테스트 코드는 여기를 [클릭](../../jmh/java/part7/StreamBenchmark.java)

* JMH는 다음 명령어를 통해 실행함 (IDE 상의 버튼으로 실행하는 건 비추.)
```bash
./gradlew clean jmh
```
* JMH 중 하나의 클래스만 실행하고 싶으면 다음 명령어로 실행
```bash
./gradlew clean jmh -Pjmh.include=(클래스이름)
```

* 해당 테스트 코드의 결과는 다음과 같음. (MacBook Air 15, M3 기준)
```text
Benchmark                      Mode  Cnt  Score   Error  Units
StreamBenchmark.parallelSum    avgt    3  3.156 ± 1.032  ms/op
StreamBenchmark.sequentialSum  avgt    3  5.688 ± 2.345  ms/op
```
* ms/op : 1번의 연산 당 걸린 시간 (ms : 1/1000초)
* 병렬이 약 1.8배 빠름

### 포크/조인 프레임워크
* 인제 병렬 스트림이 실제로 어떻게 이뤄지는지 바닥부터 보자!
* 실제로 ForkJoinSumCalculator를 만들어보면서 확인하기
* 해당 코드는 [여기](../java/part7/ForkJoinSumCalculator.java)를 참조.
* 이에 대한 테스트 코드는 여기를 [클릭](../../jmh/java/part7/ForkJoinSumBenchmark.java)
```text
Benchmark                         Mode  Cnt   Score   Error  Units
ForkJoinSumBenchmark.forkJoinSum  avgt    3  13.569 ± 2.518  ms/op
```

## Spliterator 인터페이스
* Spliterator란? iterator + split
  * 기존 iterator는 한 방향 순회만 가능.
  * Spliterator란 방향을 나눠서 순회 가능 
* 각 쓰레드별로 자료구조를 어디서부터 읽을지 가르켜주는 iterator라고 생각하면 됨!
* 왜 이걸 스트림에 쓰지? 병렬화를 안전하고 자동으로 하기 위해서

### 커스텀 Spliterator 구현
> 단어수를 세는 코드를 통해 Spliterator를 직접 만들어보자!
1. WordCounter1
* 해당 코드는 단순 반복문을 사용한다.
* [WordCounter1 코드](../java/part7/WordCounter1.java)

2. WordCounter2
* 반복문 대신 함수형 코드를 사용한다.
* [WordCounter2 코드](../java/part7/WordCounter2.java)

3. WordCounter2 + 병렬처리
* 인제 여기에 병렬처리를 할 수 있는 기능을 더해보자.
  * 중요한 포인트는 각 쓰레드가 어디부터 문장을 읽을지를 정해줘야 한다.
  * 그렇지 않으면, 쓰레드가 아무렇게나 문장을 읽어버린다.
  * 그래서 도입한 것이 바로 WordCounterSpliterator
  * 멀티쓰레딩을 위한 iterator인 Spliterator를 사용!
  * [WordCounterSpliterator 코드](../java/part7/WordCounterSpliterator.java)
* 이걸 활용해서 각 쓰레드가 읽을 부분을 지정 후에 코드를 실행
```text
Spliterator<Character> spliterator = new WordCounterSpliterator(SENTENCE);    // SENTENCE를 어떻게 쪼갤지를 규칙이 담긴 iterator를 만듬.
Stream<Character> stream2 = StreamSupport.stream(spliterator, false); // 그걸 가지고 실제 스트림을 만든다.
System.out.println("단어 수 : " + s2.countWords(stream2.parallel()));          // 병렬로 스트림을 돌린다.
```
4. [모든 내용은 여기 참조](../java/part7/WordCounterTest.java)