package part7;

import org.openjdk.jmh.annotations.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class StreamBenchmark {
    private static final long N = 10_000_000L;
    private List<Long> numbers;

    @Setup
    public void setup() {
        numbers = LongStream.rangeClosed(1, N)
                .boxed()
                .toList();
    }

    @Benchmark
    public long sequentialSum() {
        return numbers.stream()
                .mapToLong(Long::longValue)
                .sum();
    }

    @Benchmark
    public long parallelSum() {
        return numbers.parallelStream()
                .mapToLong(Long::longValue)
                .sum();
    }
}