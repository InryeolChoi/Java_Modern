package part16.Example2;

import part16.Example1.Shop;
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
    // 1. stream()으로 가격 찾기
    public List<String> findPrices(String product) {
        return shops.stream()
                .map(shop -> String.format("%s price is %.2f", shop.getName(), shop.getPrice(product)))
                .collect(toList());
    };

    // 2. parstream()으로 가격 찾기
    public List<String> findPrices2(String product) {
        return shops.parallelStream()
                .map(shop -> String.format("%s price is %.2f", shop.getName(), shop.getPrice(product)))
                .collect(toList());
    };

}
