package part16.Example2;

import part16.Example1.Shop;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class Main2 {

    private static BestPriceFinder2 finder2 = new BestPriceFinder2();

    public static void main(String[] args) {
        execute("findPrices1 : ", () -> finder2.findPrices( "myPhone27S"));
        execute("findPrices2 : ", () -> finder2.findPrices2("myPhone27S"));
        execute("findPrices3 : ", () -> finder2.findPrices3("myPhone27S"));
        execute("findPrices4 : ", () -> finder2.findPrices4("myPhone27S"));
    }



    private static void execute(String msg, Supplier<List<String>> s) {
        long start = System.nanoTime();
        System.out.println(s.get());
        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.println(msg + " done in " + duration + " msecs");
    }
}
