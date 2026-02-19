package part16.Example3;

import java.util.*;
import java.util.concurrent.*;
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

    /* 1, 가격을 찾는 메서드 : 단순 stream() */
    public List<String> findPriceWithService1(String product) {
        return shops.stream()
                .map(shop -> shop.getStringPrice(product))
                .map(Quote::parse)
                .map(Discount::applyDiscount)
                .collect(toList());
    }

    /* 2, 가격을 찾는 메서드 : completablefutuer() + executor */
    private final Executor executor = Executors.newFixedThreadPool(
            shops.size(), (Runnable r) -> {
                Thread t = new Thread(r);
                t.setDaemon(true);
                return t;
            }
    );

    public List<String> findPriceWithService2(String product) {
        List<CompletableFuture<String>> priceCF = shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(() -> shop.getStringPrice(product), executor))
                .map(future -> future.thenApply(Quote::parse))
                .map(future -> future.thenCompose(
                        quote -> CompletableFuture.supplyAsync(() -> Discount.applyDiscount(quote), executor))
                )
                .collect(toList());

        return priceCF.stream()
                .map(CompletableFuture::join)
                .collect(toList());
    };


    /* 3. 가격 + 환전 메서드 (1) */
    // 먼저 future를 이용해서 구현해보자.
    public List<String> findPriceWithService3(String product) {
        ExecutorService executor = Executors.newCachedThreadPool();
        List<Future<Double>> priceFutures = new ArrayList<>();

        for (Shop shop : shops) {
            // 첫 번째 동작 : futureRate
            final Future<Double> futureRate = executor.submit(new Callable<Double>() {
                @Override
                public Double call() {
                    return ExchangeService.getRate(ExchangeService.Money.EUR, ExchangeService.Money.USD);
                }
            });

            // 두 번째 동작 : futurePriceInUSD
            Future<Double> futurePriceInUSD = executor.submit(new Callable<Double>() {
                @Override
                public Double call() {
                    try {
                        double priceInEUR = shop.getDoublePrice(product);
                        return priceInEUR * futureRate.get();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
                }
            });

            // 두개의 future 결과를 합침.
            priceFutures.add(futurePriceInUSD);
        }

        // 두 개의 future를 받아 prices로 변환하는 부분.
        List<String> prices = new ArrayList<>();
        for (Future<Double> priceFuture : priceFutures) {
            try {
                prices.add(" price is " + priceFuture.get());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /* 3. 가격 + 환전 메서드 (1) */
    // 먼저 future를 이용해서 구현해보자.


}
