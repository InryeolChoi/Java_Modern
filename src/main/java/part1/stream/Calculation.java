package part1.stream;

import java.util.Arrays;
import java.util.List;

public class Calculation {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        System.out.println("== 병렬 스트림 시작 ==");
        numbers.parallelStream()
            .map(n -> {
                System.out.println("Thread: " + Thread.currentThread().getName() + " → " + n);
                return n * n;
            })
            .forEach(result -> {
                System.out.println("결과: " + result + " (처리한 스레드: " + Thread.currentThread().getName() + ")");
            });
        System.out.println("== 병렬 스트림 끝 ==");
    }
}
