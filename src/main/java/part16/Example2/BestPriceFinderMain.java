package part16.Example2;

import java.util.List;
import java.util.function.Supplier;

public class BestPriceFinderMain {
    private static BestPriceFinder finder = new BestPriceFinder();

    public static void main(String[] args) {

        execute("findPrices1 : ", () -> finder.findPrices("myPhone27S"));
        execute("findPrices2 : ", () -> finder.findPrices2("myPhone27S"));
        execute("findPrices3 : ", () -> finder.findPrices3("myPhone27S"));
        execute("findPrices4 : ", () -> finder.findPrices4("myPhone27S"));
    }

    private static void execute(String msg, Supplier<List<String>> s) {
        long start = System.nanoTime();
        System.out.println(s.get());
        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.println(msg + " done in " + duration + " msecs");
    }
}
