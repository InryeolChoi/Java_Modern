package part2.prac2;

public class RunnableExample {
    public static void main(String[] args) {
        Runnable task = () -> System.out.println("🧵 Runnable 실행 (람다식)");

        Thread thread = new Thread(task);
        thread.start();
    }
}
