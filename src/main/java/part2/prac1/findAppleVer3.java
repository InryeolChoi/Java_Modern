package part2.prac1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

interface ApplePredicate {
    boolean test(Apple apple);
}

class AppleHeavyWeightPredicate implements ApplePredicate {
    public boolean test(Apple apple) {
        return apple.getWeight() > 150;
    }
}

class AppleGreenColorPredicate implements ApplePredicate {
    public boolean test(Apple apple) {
        return apple.getColor() == Color.GREEN;
    }
}

class AppleGreenAndHeavyPredicate implements ApplePredicate {
    public boolean test(Apple apple) {
        return apple.getColor() == Color.GREEN && apple.getWeight() > 150;
    }
}

public class findAppleVer3 {
    public static List<Apple> thirdfilter(List<Apple> apples, ApplePredicate p) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : apples) {
            if (p.test(apple)) {
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

        // thirdfilter : 함수를 파라미터화
        List<Apple> result = thirdfilter(apples, new AppleGreenAndHeavyPredicate());
        result.forEach(System.out::println);
    }

}
