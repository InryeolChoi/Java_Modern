package part16.Example2;

public class BestPriceFinderMain {
    private static BestPriceFinder finder = new BestPriceFinder();


    public static void main(String[] args) {
        long start = System.nanoTime();

        // System.out.println(finder.findPrices("myPhone27S"));
        System.out.println(finder.findPrices2("myPhone27S"));

        long end = (System.nanoTime() - start) / 1_000_000;
        System.out.printf("물건 찾기 1 : %d msec%n", end);

    }
}
