package part15.section2;

import java.util.*;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {

        int totalRequests = 100;
        int poolSize = 10;

        // 쓰레드풀 생성
        ExecutorService executor = Executors.newFixedThreadPool(poolSize);
        List<Future<Boolean>> futures = new ArrayList<>();

        // 쓰레드 생성 및 작업
        long start = System.currentTimeMillis();
        for (int i = 1; i <= totalRequests; i++) {
            futures.add(executor.submit(new ApiCallTask(i)));
        }
        int successCount = 0;
        int completedCount = 0;

        // 쓰레드 작업을 받기
        for (Future<Boolean> future : futures) {
            try {
                Boolean completed = future.get(); // 결과 + 예외
                successCount++;
                if (completed) {
                    completedCount++;
                }
            } catch (ExecutionException | InterruptedException e) {
                System.out.println("요청 실패: " + e.getMessage());
            }
        }

        // 쓰레드 종료
        executor.shutdown();
        long end = System.currentTimeMillis();

        System.out.println("==== 결과 ====");
        System.out.println("성공 요청 수: " + successCount);
        System.out.println("completed=true 수: " + completedCount);
        System.out.println("총 소요 시간(ms): " + (end - start));
    }

}
