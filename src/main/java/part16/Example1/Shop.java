package part16.Example1;

import lombok.Getter;
import static part16.Example1.Util.*;

import java.util.Random;

public class Shop {
  @Getter
  private final String name;
  private final Random random;

  public Shop(String name) {
    this.name = name;
    random = new Random(name.charAt(0) * name.charAt(1) * name.charAt(2));
  }

  public String getPrice(String product) {
    double price = calculatePrice(product);
    Discount.Code code = Discount.Code.values()[random.nextInt(Discount.Code.values().length)];
    return name + ":" + price + ":" + code;
  }

  public double calculatePrice(String product) {
    delay();
    return format(random.nextDouble() * product.charAt(0) + product.charAt(1));
  }

  public static void delay() {
    int delay = 1000;
    //int delay = 500 + RANDOM.nextInt(2000);
    try {
      Thread.sleep(delay);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

}
