package part5.prac1;

import java.util.*;
import static java.util.stream.Collectors.toList;

public class FindVegetarian {
    public static void main(String[] args) {
        List<String> vegedishes = menu.stream()
                .filter(d -> d.isVegetarian() && d.getCalories() < 500)
                .distinct()
                .map(d -> d.getName())
                .collect(toList());
        System.out.println(vegedishes);
    }

    static List<Dish> menu = Arrays.asList(
        new Dish("pork", false, 800),
        new Dish("beef", false, 700),
        new Dish("chicken", false, 400),
        new Dish("french fries", true, 530),
        new Dish("rice", true, 350),
        new Dish("season fruit", true, 120),
        new Dish("pizza", true, 550),
        new Dish("pizza", true, 550)    // 중복 추가!
    );
}
