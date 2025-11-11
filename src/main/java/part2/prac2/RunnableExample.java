package part2.prac2;

public class RunnableExample {
    public static void run_first_option() {
        System.out.println("First Option");

        // 쓰레드가 무엇을 할지 정해주는 클래스 (익명 클래스 방식)
        // 익명클래스란? 클래스의 정의와 객체화를 동시에 하는 클래스로, 주로 일회성으로 사용)
        Runnable task = new Runnable() {
            @Override
            public void run() {
                System.out.println("🧵익명 클래스 Runnable 실행 중");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("익명 클래스 Runnable 완료");
            }
        };

        // 실제 쓰레드
        Thread t =  new Thread(task);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    public static void run_second_option() {
        System.out.println("Second Option");

        // 쓰레드가 무엇을 할지 정해주는 클래스 (람다식 방식)
        Runnable task = () -> {
            System.out.println("🧵 람다 Runnable 실행 중");
            try {
                Thread.sleep(500); // 실행 대기
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("✅ 람다 Runnable 완료");
        };

        // 실제 쓰레드
        Thread t = new Thread(task);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    public static void main(String[] args) {
        run_first_option();
        run_second_option();
        System.out.println("모든 작업 완료!");
    }
}
