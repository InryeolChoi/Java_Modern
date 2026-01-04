package part7;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.LongStream;

public class ForkJoinSumCalculator
        extends java.util.concurrent.RecursiveTask<Long> {
    private final long[] numbers;
    private final int start;
    private final int end;
    public static final long THRESHOLD = 10_000;

    // 생성자
    public ForkJoinSumCalculator(long[] numbers) {
        this(numbers, 0, numbers.length);
    }

    private ForkJoinSumCalculator(long[] numbers, int start, int end) {
        this.numbers = numbers;
        this.start = start;
        this.end = end;
    }

    public static ForkJoinSumCalculator createTask(long n) {
        long[] numbers = LongStream.rangeClosed(1, n).toArray();
        return new ForkJoinSumCalculator(numbers, 0, numbers.length);
    }

    // 메소드 1 - 분할정복 구조
    @Override
    protected Long compute() {
        int length = end - start;
        if (length < THRESHOLD) {
            return computeSequentially();
        }
        // 자식 쓰레드 생성
        ForkJoinSumCalculator leftTask =
                new ForkJoinSumCalculator(numbers, start, start + length / 2);
        leftTask.fork();

        // 부모 쓰레드의 계산
        ForkJoinSumCalculator rightTask =
                new ForkJoinSumCalculator(numbers, start + length / 2, end);
        Long rightResult = rightTask.compute();

        // 자식 쓰레드 결과와 부모 쓰레드 결과의 합 도출
        Long leftResult = leftTask.join();
        return rightResult + leftResult;
    }

    // 메소드 2 - 실제 계산 담당
    private Long computeSequentially() {
        long sum = 0;
        for (int i = start; i < end; i++) {
            sum += numbers[i];
        }
        return sum;
    }
}
