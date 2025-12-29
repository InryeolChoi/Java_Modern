package part5.prac8;

import java.util.stream.*;

public class Example3 {
    public static void main(String[] args) {
        Stream.iterate(0, n -> n + 2).limit(10)
                .forEach(System.out::println);

        Stream.generate(Math::random).limit(10).forEach(System.out::println);
    }
}
