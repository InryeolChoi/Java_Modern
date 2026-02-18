package part16.Example3;

import java.util.*;

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

    /* 가격을 찾는 메서드 */
    // 단순 stream()
    public List<String> findPriceWithService1(String product) {
        return shops.stream()
                .map(shop -> shop.getStringPrice(product))
                .map(Quote::parse)
                .map(Discount::applyDiscount)
                .collect(toList());
    }

    // parallelStream
    public List<String> findPriceWithService2(String product) {
        return shops.parallelStream()
                .map(shop -> shop.getStringPrice(product))
                .map(Quote::parse)
                .map(Discount::applyDiscount)
                .collect(toList());
    }

}
