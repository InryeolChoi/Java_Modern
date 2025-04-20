package part2.prac1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class findAppleVer4 {
    public interface Predicate<T> {
        boolean test (T item);
    }

    public static <T> List<T> fourthfilter(List<T> list, Predicate<T> p) {
        List<T> result = new ArrayList<>();
        for (T item : list) {
            if (p.test(item)) {
                result.add(item);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Random r = new Random();
        r.setSeed(System.currentTimeMillis());

        List<Apple> apples = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            int w = r.nextInt(100) + 90;
            if (i % 2 == 0)
                apples.add(new Apple(Color.GREEN, w));
            else
                apples.add(new Apple(Color.RED, w));
        }

        // 람다식 사용
        List<Apple> result = fourthfilter(apples, (Apple apple) -> apple.getColor().equals(Color.RED));

    }
}