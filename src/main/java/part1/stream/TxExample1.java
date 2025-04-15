package part1.stream;

import java.util.*;

// 구현 목표 : 리스트에서 고가의 트랜잭션만 필터링한 다음, 통화로 결과를 그룹화
// 스트림을 사용하지 않고 구현하기
public class TxExample1 {
    public static void main(String[] args) {
        List<Transaction> transcations = List.of(
                new Transaction(1200, "USD"),
                new Transaction(800, "EUR"),
                new Transaction(1500, "USD"),
                new Transaction(700, "KRW"),
                new Transaction(2000, "EUR")
        );

        Map<String, List<Transaction>> groupedByCurrency = new HashMap<>();

        for (Transaction tx : transcations) {
            if (tx.getAmount() > 1000) {
                String currency = tx.getCurrency();
                if (!groupedByCurrency.containsKey(currency)) {
                    groupedByCurrency.put(currency, new ArrayList<>());
                }
                groupedByCurrency.get(currency).add(tx);
            }
        }

        System.out.println("스트림을 사용하지 않은 경우");
        System.out.println(groupedByCurrency);
    }
}
