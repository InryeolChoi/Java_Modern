package part2.prac1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class findAppleVer1 {
    public static List<Apple> firstfilter(List<Apple> apples) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : apples) {
            if (apple.getColor() == Color.GREEN) {
                result.add(apple);
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

        // firstfilter : 녹색 사과만 판별할 수 있음.
        List<Apple> appleList1 = firstfilter(apples);
        appleList1.forEach(System.out::println);
    }
}