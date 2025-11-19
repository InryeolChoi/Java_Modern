package part5.prac3;
import java.util.*;

import static java.util.stream.Collectors.toList;

public class PrintChar {
    public static void main(String[] args) {
        List<String> words = Arrays.asList("Hello", "World");
        List<String> results = words.stream()
                .map(w -> w.split(""))
                .flatMap(Arrays::stream)
                .toList();
        System.out.println(results);
    }
}
