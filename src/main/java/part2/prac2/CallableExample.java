package part2.prac2;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class CallableExample {
    public static void main(String[] args) {
        // 쓰레드가 3개 담긴 쓰레드풀을 만드는 부분
        ExecutorService executor = Executors.newFixedThreadPool(3);
        try {
            List<Future<Integer>> firstResults = call_1st_option(executor);
            List<Future<Integer>> secondResults = call_2nd_option(executor);

            System.out.println("=== [익명 클래스 결과] ===");
            for (Future<Integer> f : firstResults) {
                System.out.println(f.get());
            }

            System.out.println("=== [람다식 결과] ===");
            for (Future<Integer> f : secondResults) {
                System.out.println(f.get());
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }
    }

    private static List<Future<Integer>> call_1st_option(ExecutorService executor) {
        List<Callable<Integer>> tasks = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            int num = i;
            tasks.add(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    System.out.println("익명 클래스로 " + num + "번째 작업 진행중..");
                    Thread.sleep(1000);
                    return num * 10;
                }
            });
        }
        try {
            return executor.invokeAll(tasks);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Future<Integer>> call_2nd_option(ExecutorService executor) {
        List<Callable<Integer>> tasks = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            int num = i;
            tasks.add(() -> {
                System.out.println("람다식으로 " + num + "번째 작업 진행중..");
                Thread.sleep(1000);
                return num * 100;
            });
        }
        try {
            return executor.invokeAll(tasks);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}