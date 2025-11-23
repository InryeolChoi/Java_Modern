package part5.prac6;

import java.util.*;

public class Example1 {
    public static void main(String[] args) {
        // 1번 문제
        List<Integer> nums = Arrays.asList(3, 7, 2, 11, 9, 5);
        Optional<Integer> max = nums.stream().reduce(Integer::max);
        System.out.println(max.orElse(0));

        // 2번 문제
        List<String> words = Arrays.asList("java", "stream", "lambda", "reduce");
        int len = words.stream().map(d->d.length()).reduce(0, Integer::sum);
        System.out.println(len);
    }
}
