package part5.prac4;

import java.util.*;
import java.math.*;

public class Example2 {
    public static void main(String[] args) {
        List<Integer> A = Arrays.asList(1, 2, 3, 4);
        List<Integer> B = Arrays.asList(5, 6, 7);

        List<String> answer = A.stream()
                .flatMap(a -> B.stream()
                        .map(b -> new AbstractMap.SimpleEntry<>(a, b))
                )
                .filter(pair ->
                        Math.abs(pair.getKey() - pair.getValue()) % 2 != 0)
                .map(pair ->
                        pair.getKey().toString() + ":" + pair.getValue().toString() +
                        "(odd)")
                .toList();

        System.out.println(answer);
    }
}
