# 함수형 관점으로 생각하기
## 선언형 프로그래밍
* 명령형 : '어떻게'에 집중한다.
* 선언형 : '무엇을'에 집중한다.

명령형 예시  
```text

```

선언형 예시  
```text
Optional<Transaction> mostExpensive = 
    transactions.stream()
                .max(comparing(Transaction::getValue));
```