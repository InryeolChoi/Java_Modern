package part16.Example3;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

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

    /* 2, 가격을 찾는 메서드 : completablefuture() + executor */
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
    // 전용 쓰레드풀 생성.
    private ExecutorService executor2 = Executors.newCachedThreadPool(r -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
    });

    public List<String> findPriceWithService3(String product) {
        List<Future<Double>> priceFutures = new ArrayList<>();

        for (Shop shop : shops) {
            // 첫 번째 동작 : futureRate
            final Future<Double> futureRate = executor2.submit(new Callable<Double>() {
                @Override
                public Double call() {
                    return Exchange.getRate(Exchange.Money.EUR, Exchange.Money.USD);
                }
            });

            // 두 번째 동작 : futurePriceInUSD
            Future<Double> futurePriceInUSD = executor2.submit(new Callable<Double>() {
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
        return prices;
    }

    /* 4. 가격 + 환전 메서드 (2) */
    // 인제 Completablefuture를 이용해서 구현해보자.
    public List<String> findPriceWithService4(String product) {
        List<CompletableFuture<String>> priceFutures = new ArrayList<>();

        // Future를 두 번 나누지 않고, 작업을 알아서 조합.
        for (Shop shop : shops) {
            CompletableFuture<String> futurePriceInUSD =
                CompletableFuture.supplyAsync(
                        () -> shop.getDoublePrice(product), executor2)
                .thenCombine(
                    CompletableFuture.supplyAsync(
                            () -> Exchange.getRate(Exchange.Money.EUR, Exchange.Money.USD), executor2)
                            .completeOnTimeout(Exchange.DEFAULT_RATE, 1, TimeUnit.SECONDS),
                        (price, rate) -> price * rate                 )
                .thenApply(price -> shop.getName() + " price is " + price);
            priceFutures.add(futurePriceInUSD);
        }

        List<String> prices = priceFutures
                .stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
        return prices;
    }

    public List<String> findPriceWithService5(String product) {
        // 스트림 + CompletableFuture
        Stream<CompletableFuture<String>> priceFuturesStream = shops.stream()
                .map(shop -> CompletableFuture
                        .supplyAsync(() -> shop.getDoublePrice(product), executor2)
                        .thenCombine(
                                CompletableFuture.supplyAsync(() -> Exchange.getRate(Exchange.Money.EUR, Exchange.Money.USD), executor2),
                                (price, rate) -> price * rate)
                        .thenApply(price -> shop.getName() + " price is " + price));

        List<CompletableFuture<String>> priceFutures = priceFuturesStream.collect(Collectors.toList());

        // .map()을 이용해 리스트로 변환
        List<String> prices = priceFutures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
        return prices;
    }

}
