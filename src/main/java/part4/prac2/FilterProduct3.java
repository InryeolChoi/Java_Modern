package part4.prac2;

import java.util.*;
import java.time.LocalDate;
import java.util.stream.Collectors;

public class FilterProduct3 {
    public static void main(String[] args) {
        LocalDate today = LocalDate.now();

        Map<Integer, LocalDate> products = orders.stream()
                .filter(p -> !p.getOrderDate().isBefore(today.minusDays(7)))
                .collect(Collectors.toMap(
                        p -> p.getId(),
                        p -> p.getOrderDate().plusDays(p.getDeliveryDays())
                ));

        System.out.println(products);
    }

    public static List<Order> orders = Arrays.asList(
            new Order(101, "alice", LocalDate.now().minusDays(1), 2),   // 유효
            new Order(102, "bob", LocalDate.now().minusDays(3), 5),     // 유효
            new Order(103, "chris", LocalDate.now().minusDays(10), 1),  // 제외
            new Order(104, "diana", LocalDate.now().minusDays(0), 1),   // 유효
            new Order(105, "eric", LocalDate.now().minusDays(8), 4)     // 제외
    );
}
