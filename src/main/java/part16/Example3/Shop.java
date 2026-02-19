package part16.Example3;

import lombok.Getter;
import java.util.Random;

public class Shop {

    @Getter
    private final String name;
    private final Random random;

    public Shop(String name) {
        this.name = name;
        random = new Random(name.charAt(0) * name.charAt(1) * name.charAt(2));
    }

    private static Util util = new Util();


    /* 가격 계산 */
    // 가격을 실제로 계산
    private double calculatePrice(String product) {
        util.delay();
        return random.nextDouble() * product.charAt(0) + product.charAt(1);
    }

    /* 가격을 가지고 오는 메소드 */
    public String getStringPrice(String product) {
        double price = calculatePrice(product);
        Discount.Code code = Discount.Code.values()[random.nextInt(Discount.Code.values().length)];
        return name + ":" + price + ":" + code;
    }

    public double getDoublePrice(String product) {
        return calculatePrice(product);
    }

}