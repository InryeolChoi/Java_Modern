package part7;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2)
@Measurement(iterations = 3)
@Fork(1)
@State(Scope.Benchmark) // 🔥 핵심 1: 벤치마크 단위로 상태 유지
public class ForkJoinSumBenchmark {

    private static final long N = 10_000_000L;

    // 🔥 핵심 2: ForkJoinPool을 재사용
    private ForkJoinPool pool;

    @Setup(Level.Trial)
    public void setUp() {
        pool = new ForkJoinPool(); // commonPool을 쓰지 않음.
    }

    @TearDown(Level.Trial)
    public void tearDown() {
        pool.shutdown(); // 스레드 정리
    }

    @Benchmark
    public long forkJoinSum() {
        return pool.invoke(ForkJoinSumCalculator.createTask(N));
    }
}

/*
* Bash에서 실행할 때의 코드는 아래와 같음.
* ./gradlew clean jmh -Pjmh.include=^part7.ForkJoinSumBenchmark$
* */