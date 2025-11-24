package part5.prac7;

import part5.prac1.Dish;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.IntStream;

public class Example {
    static List<Dish> menu = Arrays.asList(
            new Dish("pork", false, 800),
            new Dish("beef", false, 700),
            new Dish("chicken", false, 400),
            new Dish("salad", true, 150),
            new Dish("rice", true, 350),
            new Dish("fish", false, 450)
    );

    public static void main(String[] args) {
        int sum = menu.stream().mapToInt(Dish::getCalories).sum();
        System.out.println(sum);

        OptionalDouble avg = menu.stream().mapToDouble(Dish::getCalories).average();
        System.out.println(avg.orElse(0));

        int evensum = IntStream.rangeClosed(1, 100).filter(i -> i % 2 == 0).sum();
        System.out.println(evensum);
    }
}
