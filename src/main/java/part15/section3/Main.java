package part15.section3;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        int totalRequests = 100;

        long start = System.currentTimeMillis();

        // 1) 요청 파이프라인 생성
        List<CompletableFuture<Integer>> tasks =
                IntStream.rangeClosed(1, totalRequests)
                        .mapToObj(id -> AsyncApiClient
                                .callTodoAsync(id)
                                .thenApply(completed -> completed ? 1 : 0)
                        )
                        .toList();

        // 2) 전부 완료되면 합산
        CompletableFuture<Integer> result =
                CompletableFuture
                        .allOf(tasks.toArray(new CompletableFuture[0]))
                        .thenApply(v ->
                                tasks.stream()
                                        .mapToInt(CompletableFuture::join)
                                        .sum()
                        );

        int completedCount = result.join(); // 여기서만 대기

        long end = System.currentTimeMillis();

        System.out.println("==== 결과 ====");
        System.out.println("completed=true 수: " + completedCount);
        System.out.println("총 소요 시간(ms): " + (end - start));

    }
}
