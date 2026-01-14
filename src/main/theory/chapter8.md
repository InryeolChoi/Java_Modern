___# 8. 컬랙션 API 개선 
## 컬랙션 팩토리
> 👉 “간단하고, 안전한 불변 컬렉션을 만들기 위한 API”

* 블변 객체, 만들기 그렇게 힘든가?
1. 배열(Arrays)을 변환 -> 불필요한 객체 생성
2. 집합(Set)을 변환 -> 불필요한 객체 생성
3. final 붙이기 -> 참조 금지만 되지, 내부는 변경할 수 있음. 
* 자세히 설명하자면, 참조 금지라는 건 C++ 레퍼런스/포인터처럼,
  * 자료구조의 ‘주소(객체 식별자)’를 다시 가리키는 걸 막는 거고,
  * 그 자료구조 안의 내용물까지 막는 건 아니다.
* 자바에서 직접 주소 연산을 안 하니까,
* final은 new로 생성된 다른 객체로의 재할당을 막는 의미로 이해하면 된다
```text
final List<String> roles = new ArrayList<>();

// 이건 안됨.
roles = new ArrayList<>();

// 그러나 이건 됨.
roles.add("USER");
roles.add("ADMIN");
```

* 그래서 나온 of() 메소드
1. 불변(Immutable) -> add/remove/clear 시도시 UnsupportedOperationException 바로 나옴.
2. null 허용 안 함 -> NullPointerException
3. 간결함 = 실수 감소 -> 의도를 명확히 보여줌 (붑변임)

* 단, 이런 건 안해준다.
1. 깊은 불변성
   * ex) 이중 리스트인 경우, 겉 리스트는 불변이나 속 리스트는 불변이 아님.
2. CRUD 동작

* of를 쓰면 좋은 경우 👍
1. 상수 데이터 
2. 설정 값 
3. 테스트 픽스처 
4. enum 대체 데이터 

* 쓰면 안 되는 경우 👎 
1. add/remove가 핵심인 로직
2. 사용자 입력 누적
3. 상태 변화가 중요한 도메인

* [예시코드는 이곳을 클릭](../java/part8/example1.java)

## 리스트와 집합처리
* 자바 8에서 List, Set 인터페이스 추가된 메서드

### `removeIf()` 메서드
* 직접 iterator를 관리하지 않고 원하는 자료구조 내의 데이터를 삭제할 수 있음.
* ConcurrentModificationException를 방지할 수 있음.
  * ConcurrentModificationException란? 하나의 컬렉션을 순회(iteration) 중에 구조를 변경(structural modification)했을 때 발생하는 런타임
  * 동시에 순회와 수정이 발생하면 생기는 오류
* 단, 불변 컬렉션(선언할 때 of 메소드를 쓴 자료구조)는 쓸 수 없음.
* [예시 : removeIf()](../java/part8/example2.java)

### `removeAll()` 메서드
* 리스트의 모든 요소를 주어진 규칙으로 치환하는 메소드
* 전체 요소를 한 번씩만 처리하며, 인덱스 관리 불필요하고, 명확한 의도 표현이 가능함.
* [예시 : removeAll()](../java/part8/example2.java)

# 8.3 Map API 개선
> Map을 위한 여러가지 메서드 

1. `forEach()` 메서드 : 맵의 키/값을 반복하면서 확인해야 하는 문제를 해결하기에 적합
2. 정렬 메서드 : 값이나 키를 기준으로 정렬하는 메소드
3. `getOrDefault()` 메서드 : NullPointerException을 방지하기 위한 메서드. 찾으려는 키가 없으면 
기본값을 반환해준다.
4. 계산 패턴 : 맵에 키가 존재하는지의 여부에 따라 동작을 실행하는 메소드들
5. 삭제 패턴 : 맵에 키가 특정한 값과 연관되었을 때만의 항목을 제거하는 오버로드 버전의 `remove()`
6. `Replace()`, `replaceAll()` : 맵의 항목 변경
7. `merge()` : 두 맵을 합치는 메소드

* [예시코드는 이곳을 클릭](../java/part8/example3.java)

# 개선된 ConcurrentHashMap
> ConcurrentHashMap이란? 동시성 친화적인 HashMap
> 내부적으로 락 분할(lock striping) 또는 CAS를 사용해 여러 스레드가 동시에 다른 영역에 접근 가능

## 리듀스와 검색
* `forEach()`, `reduce()`, `search()`
* 세 가지 메소드 모두 조건부 병렬화가 가능. 첫 번째 인자에는 Int 값, 두 번째 인자에는 키와 값을 집어넣음.  
맵 크기가 이 값보다 크면 병렬로 실행, 작으면 단일 스레드로 실행.
* 첫 번째 인자를 parallelismThreshold라고 하며, 병렬 오버헤드 방지 장치
  * parallelismThreshold는 null 허용 안 함
  * 순서 보장 없음
* [예시코드는 이곳을 클릭](../java/part8/example4.java)

## 계수
* `mappingCount()` : 맵의 매핑 갯수를 반환하는 메소드.
* `size()`는 안되나? `size()`는 정확한 값을 보장하려면 내부적으로 모든 버킷을 검사해야 함.
* 따라서 size보다 동시성 친화적인 `mappingCount()`가 나옴.
* [예시코드는 이곳을 클릭](../java/part8/example4.java)

## 집합뷰
* `keySet()` : ConcurrentHashMap을 집합으로 바꿀 수 있는 메소드.
* 
* 
* [예시코드는 이곳을 클릭](../java/part8/example4.java)
