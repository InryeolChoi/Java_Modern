package part1.stream;

import java.util.*;

public class Example1 {
    public static void main(String[] args) {
        List<Transaction> transactions = List.of(
                new Transaction(1200, "USD"),
                new Transaction(800, "EUR"),
                new Transaction(1500, "USD"),
                new Transaction(700, "KRW"),
                new Transaction(2000, "EUR")
        );

        Map<String, List<Transaction>> groupedByCurrency = new HashMap<>();
        for (Transaction tx : transactions) {
            if (tx.getAmount() > 1000) {
                String currency = tx.getCurrency();
                if (!groupedByCurrency.containsKey(currency)) {
                    groupedByCurrency.put(currency, new ArrayList<>());
                }
                groupedByCurrency.get(currency).add(tx);
            }
        }

        System.out.println(groupedByCurrency);

    }
}
