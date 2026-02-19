package part16.Example4;

import java.util.List;
import java.util.function.Supplier;

public class BestPriceFinderMain {
    private static BestPriceFinder newservice = new BestPriceFinder();

    public static void main(String[] args) {
        // allOf 사용
        execute("findPricesWithAllOf : ", () -> newservice.findPricesWithAllOf("myPhone27S"));

        // anyOf 사용
        execute2("findFastestPrice : ", () -> newservice.findFastestPrice("myPhone27S"));
    }

    private static void execute (String msg, Supplier<List<String>> s){
        long start = System.nanoTime();
        System.out.println(s.get());
        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.println(msg + " done in " + duration + " msecs");
    }

    private static void execute2 (String msg, Supplier<String> s){
        long start = System.nanoTime();
        System.out.println(s.get());
        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.println(msg + " done in " + duration + " msecs");
    }
}
