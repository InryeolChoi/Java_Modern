package part5.prac4;

import java.util.*;
import static java.util.stream.Collectors.toList;

public class Example1 {
    public static void main(String[] args) {
        List<String> words1 = Arrays.asList("java", "spring", "cloud");
        List<String> words2 = Arrays.asList("boot", "core", "ai");

        List<String> JointWords = words1.stream()
                .flatMap(i -> words2.stream().map(j -> i + "-" + j))
                .filter(newstr -> {
                    String[] parts = newstr.split("-");
                    return (parts[0].length() + parts[1].length()) >= 10;
                })
                .map(s1 -> {
                    String[] parts = s1.split("-");
                    int s1_len = parts[0].length() + parts[1].length();
                    return s1 + "(" + s1_len + ")";
                })
                .collect(toList());

        System.out.println(JointWords);
    }
}
