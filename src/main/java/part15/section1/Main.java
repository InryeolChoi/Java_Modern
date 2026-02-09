package part15.section1;

import java.util.*;
import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        int totalRequests = 100;
        int maxConcurrent = 10; // 동시에 10개만 실행

        List<Thread> threads = new ArrayList<>();
        Semaphore semaphore = new Semaphore(maxConcurrent);
        ResultCounter counter = new ResultCounter();

        long start = System.currentTimeMillis();
        for (int i = 1; i <= totalRequests; i++) {
            ApiTask task = new ApiTask(i, counter, semaphore);
            Thread thread = new Thread(task);
            threads.add(thread);
            thread.start();
        }

        // 모든 쓰레드 종료 대기
        for (Thread t : threads) {
            t.join();
        }

        long end = System.currentTimeMillis();

        System.out.println("==== 결과 ====");
        System.out.println("성공 요청 수: " + counter.getSuccessCount());
        System.out.println("completed=true 수: " + counter.getCompletedCount());
        System.out.println("총 소요 시간(ms): " + (end - start));

    }
}
