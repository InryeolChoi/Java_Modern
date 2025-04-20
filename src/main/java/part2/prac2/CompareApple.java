package part2.prac2;

import part2.prac1.Apple;
import part2.prac1.Color;

import java.util.*;

public class CompareApple {
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

        apples.sort((Apple a1, Apple a2) -> Comparator.comparingInt(Apple::getWeight).compare(a1, a2));
        System.out.println(apples);
    }

}
