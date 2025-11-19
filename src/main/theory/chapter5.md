# 5. 스트림 활용
## 필터링
### 1) filter(Predicate 조건)
* 스트림 요소들 중 특정 조건을 만족하는 요소만 선택하는 연산.
```java
stream.filter(x -> x > 10)
```
* filter는 중간 연산 (lazy)
* Predicate<T>를 함수로 받음 (true면 남기고, false면 제거)
* 원본 리스트는 변경하지 않음 (불변)

### 2) distinct() — 중복 제거

스트림에서 중복된 요소를 제거할 수 있음.