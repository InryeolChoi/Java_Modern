package part4.prac2;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

public class FilterProduct2 {
    public static void main(String[] args) {
        Map<String, Double> result = products.stream()
                .collect(Collectors.groupingBy(
                        Product::getCategory,
                        Collectors.averagingInt(Product::getPrice)
                ));
        System.out.println(result);
    }

    public static List<Product> products = Arrays.asList(
            new Product("iPhone 16", "Electronics", 1250000, 4.8, 12000),
            new Product("Galaxy S25", "Electronics", 1100000, 4.5, 9000),
            new Product("Sony WH-1000XM5", "Electronics", 380000, 4.7, 6000),

            new Product("Zara Oversized Hoodie", "Fashion", 59000, 4.2, 4000),
            new Product("Nike Air Max 2025", "Fashion", 189000, 4.6, 8000),
            new Product("Uniqlo Sweatshirt", "Fashion", 39000, 4.1, 3500),

            new Product("Harry Potter 1", "Book", 17000, 4.9, 20000),
            new Product("Effective Java 3/E", "Book", 46000, 4.8, 3000),
            new Product("Clean Code", "Book", 37000, 4.7, 4500)
    );
}
