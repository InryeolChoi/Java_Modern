package part7;

import java.util.stream.Stream;
import java.util.Scanner;

public class seqSum {
    public static long parallelSum(long n) {
        return Stream.iterate(1L, i -> i + 1)
                .limit(n)
                .parallel()
                .reduce(0L, Long::sum);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int threadnum = Runtime.getRuntime().availableProcessors() - 1;
        System.out.println("현재 쓰레드풀 내 쓰래드 갯수 : " + threadnum);

        System.out.println("1 ~ n을 더한다고 가정했을 때, n을 입력해봅시다.");
        long result = parallelSum(sc.nextLong());
        System.out.println("1 ~ n까지의 합 : " + result);
    }
}
