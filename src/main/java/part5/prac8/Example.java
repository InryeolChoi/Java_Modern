package part5.prac8;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.*;

public class Example {
    public static void main(String[] args) {
        // 값에서 스트림 만들기
        Stream<String> s1 = Stream.of("Modern ", "Java ", "In ", "Action");
        s1.map(String::toUpperCase).forEach(System.out::println);

        Stream<Stream> emptyStream = Stream.empty();

        // 배열로 스트림 만들기 1
        int[] numbers = {1,2,3,4,5,6};
        IntStream intstream = Arrays.stream(numbers);

        // 배열로 스트림 만들기 2
        Stream<int[]> s = Stream.of(new int[]{1,2,3});
    }
}
