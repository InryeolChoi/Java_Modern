package part5.prac7;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Example2 {
    public static void main(String[] args) {
        Stream<int[]> pythagoreanTriples = IntStream.rangeClosed(1, 100).boxed()
                .flatMap(a -> IntStream.rangeClosed(a, 100)
                        .filter(b -> Math.sqrt(a * a + b * b ) % 1 == 0)
                        .mapToObj(b -> new int[]{a, b, (int)Math.sqrt(a*a + b*b)})
                );

        Stream<double[]> pythagoreanTriples2 = IntStream.rangeClosed(1, 100).boxed()
                .flatMap(a -> IntStream.rangeClosed(a, 100)
                        .mapToObj(b -> new double[]{a, b, Math.sqrt(a*a + b*b)})
                        .filter(t -> t[2] % 1 == 0)
                );

        pythagoreanTriples2.limit(10).forEach(t -> System.out.println(Arrays.toString(t)));
    }
}
