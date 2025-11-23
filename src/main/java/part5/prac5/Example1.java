package part5.prac5;

import part5.prac1.Dish;
import java.util.*;

public class Example1 {
    List<Dish> menu = Arrays.asList(
        new Dish("pork", false, 800),
        new Dish("beef", false, 700),
        new Dish("chicken", false, 400),
        new Dish("salad", true, 150),
        new Dish("rice", true, 350),
        new Dish("fish", false, 450)
    );

    public static void main(String[] args) {
        Example1 example = new Example1();

        // 1번 문항
        Boolean ans1 = example.menu.stream()
                .anyMatch(d -> d.getCalories() >= 600);
        System.out.println("ans1 : " + ans1);

        // 2번 문항
        Boolean ans2 = example.menu.stream()
                .allMatch(d -> d.getCalories() < 900);
        System.out.println("ans2 : " + ans2);

        // 3번 문항
        Boolean ans3 = example.menu.stream()
                .noneMatch(d -> d.getCalories() >= 1000);
        System.out.println("ans3 : " + ans3);

        // 4번 문항
        Dish lowestCalDish = example.menu.stream()
                .filter(Dish::isVegetarian)
                .sorted(Comparator.comparing(Dish::getCalories))
                .findFirst().get();
        System.out.println("ans4 : " + lowestCalDish);

        // 5번 문항
        Dish anyDishLowerThan500Cal = example.menu.stream()
                .filter(d -> d.getCalories() < 500)
                .findAny().get();
        System.out.println("ans5 : " + anyDishLowerThan500Cal);

    }
}
