package part5.prac2;
import part5.prac1.Dish;
import static java.util.stream.Collectors.toList;
import java.util.*;

public class FindFirstTwo {
    public static void main(String[] args) {
        List<String> firstTwoMenu = menu.stream()
                .takeWhile(d -> d.getCalories() < 500)
                .limit(2)
                .map(d -> d.getName())
                .collect(toList());
        System.out.println(firstTwoMenu);

        record DishInfo(String name, int calories) {}
        List<DishInfo> infoList = menu.stream()
                .takeWhile(d -> d.getCalories() < 500)
                .limit(2)
                .map(d -> new DishInfo(d.getName(), d.getCalories()))
                .collect(toList());
        System.out.println(infoList);
    }

    static List<Dish> menu = Arrays.asList(
        new Dish("season fruit", true, 120),
        new Dish("rice", true, 350),
        new Dish("salmon", false, 450),
        new Dish("pizza", true, 550),
        new Dish("pork", false, 800),
        new Dish("beef", false, 700),
        new Dish("chicken", false, 400)
    );
}
