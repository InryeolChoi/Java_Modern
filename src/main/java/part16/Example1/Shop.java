package part16.Example1;

import lombok.Getter;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class Shop {

    @Getter
    private final String name;
    private final Random random;

    public Shop(String name) {
        this.name = name;
        random = new Random(name.charAt(0) * name.charAt(1) * name.charAt(2));
    }

    /* 가격 계산 */
    // 가격을 실제로 계산
    private double calculatePrice(String product) {
        delay();
        return random.nextDouble() * product.charAt(0) + product.charAt(1);
    }

    // 일부러 delay를 시킨다.
    public static void delay() {
        int delay = 1000;
        //int delay = 500 + RANDOM.nextInt(2000);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /* 가격을 가지고 오는 메소드 */
    // 1. 동기로 설계하기
    public double getPrice(String product) {
        return calculatePrice(product);
    }

    // 2. 비동기로 설계하기
    public Future<Double> getPriceAsync(String product) {
        CompletableFuture<Double> futurePrice = new CompletableFuture<>();
        new Thread(() -> {
            double price = calculatePrice(product);
            futurePrice.complete(price);
        }).start();
        return futurePrice;
    }

    // 3. Try-Catch로 오류처리를 추가
    public Future<Double> getPriceAsync2(String product) {
        CompletableFuture<Double> futurePrice = new CompletableFuture<>();
        new Thread(() -> {
            try {
                double price = calculatePrice(product);
                futurePrice.complete(price);
            } catch (Exception ex) {
                futurePrice.completeExceptionally(ex);
            }
        }).start();
        return futurePrice;
    }

    // 4. CompletableFuture.supplyAsync()로 단순화하기
    public Future<Double> getPriceAsync3(String product) {
        return CompletableFuture.supplyAsync(() -> calculatePrice(product));
    }
}