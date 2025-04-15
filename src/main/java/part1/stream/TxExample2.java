package part1.stream;

import java.util.*;
import java.util.stream.Collectors;

public class TxExample2 {
    public static void main(String[] args) {
        List<Transaction> transcations = List.of(
                new Transaction(1200, "USD"),
                new Transaction(800, "EUR"),
                new Transaction(1500, "USD"),
                new Transaction(700, "KRW"),
                new Transaction(2000, "EUR")
        );

        Map<String, List<Transaction>> groupedByCurrency = transcations.stream()
                .filter(tx -> tx.getAmount() > 1000)
                .collect(Collectors.groupingBy(Transaction::getCurrency));

        System.out.println("스트림을 사용한 경우");
        System.out.println(groupedByCurrency);
    }
}