package part5.prac6;

import part5.prac1.Dish;
import java.util.*;

public class Example2 {
    static List<Dish> menu = Arrays.asList(
        new Dish("pork", false, 800),
        new Dish("beef", false, 700),
        new Dish("chicken", false, 400),
        new Dish("salad", true, 150),
        new Dish("rice", true, 350),
        new Dish("fish", false, 450)
    );

    public static void main(String[] args) {
        // 1번째 문제
        int total = menu.stream()
                .reduce(0, (s, dish) -> s + dish.getCalories(), Integer::sum);
        System.out.println("Total Calories: " + total);

        // 2번째 문제
        Dish heaviestDish = menu.stream()
                .reduce((d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2).get();
        System.out.println("Heaviest Dish: " + heaviestDish);

        // 3번째 문제
        String names = menu.stream()
                .reduce("", (name, dish) -> name + dish.getName(), String::concat);
        System.out.println("Names: " + names);

        // 4번째 문제
        int count = menu.stream()
                .reduce(0, (s, d) -> s + 1, Integer::sum);
        System.out.println("Count: " + count);
    }
}
