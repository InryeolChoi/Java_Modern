package part1.lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.function.*;

public class CheckApple {
    public static boolean isGreenApple(Apple apple) {
        return "GREEN".equals(apple.getColor());
    }

    public static boolean isHeavyApple(Apple apple) {
        return apple.getWeight() > 150;
    }

    static List<Apple> filterApples(List<Apple> inventory, Predicate<Apple> predicate) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (predicate.test(apple)) {
                result.add(apple);
            }
        }
        return result;
    }
}