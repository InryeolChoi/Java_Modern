package part3.prac3;

import java.util.function.Predicate;

public class PredicateExample {
    public static void main(String[] args) {
        // 1) 익명 클래스 방식
        Predicate<Integer> isEven1 = new Predicate<Integer>() {
            @Override
            public boolean test(Integer n) {
                return n % 2 == 0;
            }
        };

        // 2) 람다식 방식
        Predicate<Integer> isEven2 = n -> n % 2 == 0;

        System.out.println(isEven1.test(0));
        System.out.println(isEven2.test(1));
    }

}
