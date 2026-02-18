package part16.Example2;

import part16.Example1.Shop;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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

    /* 가격을 찾는 메서드 */
    // 1. stream()으로 가격 찾기
    public List<String> findPrices(String product) {
        return shops.stream()
                .map(shop -> String.format("%s price is %.2f", shop.getName(), shop.getPrice(product)))
                .collect(toList());
    };

    // 2. parallelStream()으로 가격 찾기
    public List<String> findPrices2(String product) {
        return shops.parallelStream()
                .map(shop ->
                        String.format("%s price is %.2f", shop.getName(), shop.getPrice(product)))
                .collect(toList());
    };

    // 3. CompletableFuture.supplyAsync()로 이용하기
    public List<String> findPrices3(String product) {
        List<CompletableFuture<String>> prices = shops
                .parallelStream()
                .map(shop ->
                    CompletableFuture.supplyAsync(() ->
                        String.format("%s price is %.2f", shop.getName(), shop.getPrice(product))
                    )
                ).collect(toList());

        return prices.stream()
                .map(CompletableFuture::join)
                .collect(toList());
    };

    public List<String> findPrices4(String product) {
        List<CompletableFuture<String>> prices = shops
                .parallelStream()
                .map(shop ->
                    CompletableFuture.supplyAsync(() ->
                        String.format("%s price is %.2f", shop.getName(), shop.getPrice(product)),
                        executor)
                ).collect(toList());

        return prices.stream()
                .map(CompletableFuture::join)
                .collect(toList());
    };
}
