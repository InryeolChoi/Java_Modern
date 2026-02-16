package part16.Example1;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ShopMain {
    public static void main(String[] args) {
        Shop shop = new Shop("BestShop");

        long start = System.nanoTime();
        System.out.println("자식 메서드의 분기 시작");
        Future<Double> futurePrice = shop.getPriceAsync2("my favorite product");

        long invocationTime = ((System.nanoTime() - start) / 1_000_000);
        System.out.println("자식 메서드의 반환(invocation)이 이뤄짐");
        System.out.println("걸린 시간 : " + invocationTime + " msecs");

        // 가격을 계산하는 동안, 메인 메서드가 다른 작업을 수행
        doSomethingElse();

        try {
            double price = futurePrice.get();
            System.out.printf("가격은 %.2f%n", price);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        long retrievalTime = ((System.nanoTime() - start) / 1_000_000);
        System.out.println("자식 메서드의 결과(가격)은 " + retrievalTime + " msecs 후에 도착함.");
    }

    private static void doSomethingElse() {
        System.out.println("메인 메서드 : 다른 작업 수행 중...");
    }
}
