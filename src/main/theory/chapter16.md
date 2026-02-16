# Completablefuture: 조합 가능한 비동기 프로그래밍

* 오늘날은 여러 개의 처리 자원(CPU 코어 등)이 존재하며, 
* 우리는 프로그램이 이 자원들을 최대한 고수준(high-level) 방식으로 활용하기를 원한다.
* 즉, 스레드를 직접 지저분하게 다루는 비구조적이고 유지보수 어려운 코드를 피하고자 한다.

우리는 다음을 이미 봤다:
* 병렬 스트림(parallel streams)
* fork/join 병렬성

이들은
* 컬렉션 반복 작업 
* 분할 정복(divide-and-conquer) 알고리즘
과 같은 경우에 고수준 병렬 표현을 제공한다.

하지만 메서드 호출 자체도 병렬 실행의 기회가 될 수 있다.  

Java 8과 9는 이를 위해 두 가지 API를 도입했다:
1.	CompletableFuture
2.	리액티브 프로그래밍 패러다임

이 장에서는 CompletableFuture를 실전코드로 다뤄보며 연습한다고 생각.

## Future의 도입과 한계
**Future의 장점**
1. 