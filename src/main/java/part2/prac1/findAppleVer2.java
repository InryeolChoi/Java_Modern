package part2.prac1;
import java.util.*;

public class findAppleVer2 {
    public static List<Apple> secondfilter(List<Apple> apples, Color color) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : apples) {
            if (apple.getColor() == color) {
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

        // secondfilter : 색을 파라미터화
        System.out.println("Green Apple");
        List<Apple> greenAppleList = secondfilter(apples, Color.GREEN);
        greenAppleList.forEach(System.out::println);

        System.out.println("Red Apple");
        List<Apple> redAppleList = secondfilter(apples, Color.RED);
        redAppleList.forEach(System.out::println);
    }
}
