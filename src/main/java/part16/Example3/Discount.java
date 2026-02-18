package part16.Example3;

import lombok.AllArgsConstructor;

import static part16.Example3.Util.format;

public class Discount {
    @AllArgsConstructor
    public enum Code {
        NONE(0),
        SILVER(5),
        GOLD(10),
        PLATINUM(15),
        DIAMOND(20);

        private final int percentage;
    };

    private static Util util = new Util();

    public static String applyDiscount(Quote quote) {
        return quote.getShopName() + " price is " + apply(quote.getPrice(), quote.getDiscountCode());
    }

    private static double apply(double price, Code code) {
        util.delay();
        return format(price * (100 - code.percentage) / 100);
    }

}
