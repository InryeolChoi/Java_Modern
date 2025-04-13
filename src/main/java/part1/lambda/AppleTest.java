package part1.lambda;

import java.util.List;
import java.util.ArrayList;

public class AppleTest {
    public static void main(String[] args) {
        List<Apple> inventory = new ArrayList<>();
        inventory.add(new Apple("GREEN", 160));
        inventory.add(new Apple("RED", 120));
        inventory.add(new Apple("GREEN", 140));
        inventory.add(new Apple("YELLOW", 170));

        System.out.println("🍏 Green Apples:");
        List<Apple> greenApples = CheckApple.filterApples(inventory, CheckApple::isGreenApple);
        greenApples.forEach(System.out::println);

        System.out.println("\n⚖️ Heavy Apples (> 150g):");
        List<Apple> heavyApples = CheckApple.filterApples(inventory, CheckApple::isHeavyApple);
        heavyApples.forEach(System.out::println);

        // 람다식 사용
        System.out.println("\n🍎 Red Apples (람다식 사용):");
        List<Apple> redApples = CheckApple.filterApples(inventory, apple -> "RED".equals(apple.getColor()));
        redApples.forEach(System.out::println);
    }
}
