package part16.Example4;

import part16.Example3.Discount;
import part16.Example3.Exchange;
import part16.Example3.Quote;
import part16.Example3.Shop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class BestPriceFinder {
    // 5가지의 Shop을 정의한다.
    private final List<Shop> shops = Arrays.asList(
            new Shop("BestPrice"),
            new Shop("LetsSaveBig"),
            new Shop("MyFavoriteShop"),
            new Shop("BuyItAll"),
            new Shop("ShopEasy")
    );


    private final Executor executor = Executors.newFixedThreadPool(
            shops.size(), (Runnable r) -> {
                Thread t = new Thread(r);
                t.setDaemon(true);
                return t;
            }
    );

    /* 1. allOf() 활용 */
    public List<String> findPricesWithAllOf(String product) {
        List<CompletableFuture<String>> futures =
                shops.stream()
                        .map(shop ->
                                CompletableFuture.supplyAsync(
                                        () -> String.format(
                                                "%s price is %.2f",
                                                shop.getName(),
                                                shop.getDoublePrice(product)
                                        ),
                                        executor
                                )
                        )
                        .toList();

        // 모든 비동기 작업 완료 기다림
        CompletableFuture<Void> allDone =
                CompletableFuture.allOf(
                        futures.toArray(new CompletableFuture[0])
                );

        // 완료 후 결과 수집
        return allDone.thenApply(v ->
                futures.stream()
                        .map(CompletableFuture::join)
                        .toList()
        ).join();
    }

    /* 2. anyOf() 활용 */
    public String findFastestPrice(String product) {
        List<CompletableFuture<String>> futures =
                shops.stream()
                        .map(shop ->
                                CompletableFuture.supplyAsync(
                                        () -> String.format(
                                                "%s price is %.2f",
                                                shop.getName(),
                                                shop.getDoublePrice(product)
                                        ),
                                        executor
                                )
                        )
                        .toList();

        CompletableFuture<Object> fastest =
                CompletableFuture.anyOf(
                        futures.toArray(new CompletableFuture[0])
                );

        return (String) fastest.join();
    }
}
