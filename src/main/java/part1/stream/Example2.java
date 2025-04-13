package part1.stream;

import java.util.*;
import static java.util.stream.Collectors.*;

public class Example2 {
    public static void main(String[] args) {
        List<Transaction> transactions = List.of(
                new Transaction(1200, "USD"),
                new Transaction(800, "EUR"),
                new Transaction(1500, "USD"),
                new Transaction(700, "KRW"),
                new Transaction(2000, "EUR")
        );

        Map<String, List<Transaction>> groupedByCurrency = transactions.stream()
                .filter(tx -> tx.getAmount() > 1000)
                .collect(groupingBy(Transaction::getCurrency));
        System.out.println(groupedByCurrency);

    }

}
