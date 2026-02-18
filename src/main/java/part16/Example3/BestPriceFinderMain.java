package part16.Example3;

import java.util.List;
import java.util.function.Supplier;

public class BestPriceFinderMain {
    private static BestPriceFinder service = new BestPriceFinder();

    public static void main(String[] args) {
        execute("findPriceWithService1 : ", () -> service.findPriceWithService1("myPhone27S"));
        execute("findPriceWithService2 : ", () -> service.findPriceWithService2("myPhone27S"));
    }

    private static void execute (String msg, Supplier<List<String>> s){
        long start = System.nanoTime();
        System.out.println(s.get());
        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.println(msg + " done in " + duration + " msecs");
    }
}
