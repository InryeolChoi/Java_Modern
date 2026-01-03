package part6;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class example {
    public static void main(String[] args) {
        List<Dish> menu = List.of(
                new Dish("pork", false, 800, Dish.Type.MEAT),
                new Dish("beef", false, 700, Dish.Type.MEAT),
                new Dish("chicken", false, 400, Dish.Type.MEAT),
                new Dish("french fries", true, 530, Dish.Type.OTHER),
                new Dish("rice", true, 350, Dish.Type.OTHER),
                new Dish("season fruit", true, 120, Dish.Type.OTHER),
                new Dish("pizza", true, 550, Dish.Type.OTHER),
                new Dish("prawns", false, 300, Dish.Type.FISH),
                new Dish("salmon", false, 450, Dish.Type.FISH)
        );

        long menu_type = menu.stream().count();
        System.out.println("menu_type : " + menu_type);

        Map<Dish.Type, Long> dishes_by_type = menu.stream().collect(groupingBy(Dish::getType, counting()));
        System.out.println("dishes_by_type : " + dishes_by_type);

        Map<Dish.Type, Double> avg_cal_by_type = menu.stream()
                .collect(groupingBy(Dish::getType, averagingDouble(Dish::getCalories)));
        System.out.println("avg_cal_by_type : " + avg_cal_by_type);

        Map<Boolean, List<Dish>> isVegan = menu.stream()
                .collect(Collectors.partitioningBy(Dish::isVegetarian));
        System.out.println("isVegan : " + isVegan);

        Optional<Dish> highestCal = menu.stream().max(comparingInt(Dish::getCalories));
        System.out.println("highest_cal : " + highestCal);

    }
}
