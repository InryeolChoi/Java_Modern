# 6. 스트림으로 데이터 수집 (컬렉터의 활용)
## 컬렉터란 무엇인가?
* 스트림 연산의 분류
  * 중간 연산: map, filter, sorted …
  * 최종 연산: forEach, reduce, collect
* collect()는 스트림을 소비해서 최종 결과를 만들어내는 연산
* collect()의 역할 : 스트림의 요소들을 누적(accumulate) 해서 하나의 결과 컨테이너(List, Map, 숫자 등)로 변환한다  

🔍 collect()의 메서드 시그니처
```text
<R, A> R collect(Collector<? super T, A, R> collector)
```
* 즉, 스트림 요소 T를 하나씩 받아 
* 중간 저장소 A에 누적한 뒤 
* 최종 결과 R로 변환

## 컬렉터 활용하기
* 이 장에서는 이론을 정리하는 것보다는, 쓰임새를 알아보고 가는데 집중한다.
* 따라서 6.1 ~ 6.4 장까지의 내용만 가볍게 짚고 넘어감
* 6.5 ~ 6.6장 (Collector 인터페이스 살펴보기, 커스텀 컬렉터 만들기)는 안함.
* 핵심 사고 방식 
  * “이 스트림 결과를 어떤 형태로 받고 싶은가?”
  •	List → toList()
  •	개수 → counting()
  •	합/평균 → summing / averaging
  •	분류 → groupingBy()
  •	조건 분리 → partitioningBy()

* collect 기본 구조
```text
import java.util.stream.Collectors

// 기본 구조
stream.collect(Collector)
stream.collect(Collectors.xxx())
```

## 가장 많이 쓰는 Collectors
1. List로 수집 
2. 개수 세기 
3. 합계 / 평균
4. 문자열 합치기 
5. groupingBy
6. partitioningBy

[연습문제](../java/part6/example.java)