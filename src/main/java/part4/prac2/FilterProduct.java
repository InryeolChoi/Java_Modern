package part4.prac2;

import java.util.*;

import static java.util.stream.Collectors.toList;

public class FilterProduct {
    public static void main(String[] args) {
        List<String> filteredProducts = products.stream()
                .filter(p -> p.getCategory().equals("Electronics"))
                .filter(p -> p.getRating() >= 4.5)
                .sorted(Comparator.comparing(Product::getSales).reversed())
                .map(Product::getName)
                .collect(toList());

        System.out.println(filteredProducts);
    }

    public static List<Product> products = Arrays.asList(
        new Product("Galaxy S25", "Electronics", 500, 4.5, 2),
        new Product("Apple", "Food",5, 4, 4),
        new Product("iPhone 17", "Electronics",600, 4.7, 1),
        new Product("Banana", "Food",6, 4, 3),
        new Product("Zara shirts", "Clothing",7, 4, 6),
        new Product("iPhone 17", "Electronics",700, 3.7, 5)
    );
}
