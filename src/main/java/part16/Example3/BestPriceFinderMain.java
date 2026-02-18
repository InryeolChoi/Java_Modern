package part16.Example3;

public class BestPriceFinderMain {
    private static BestPriceFinder finder = new BestPriceFinder();

    public static void main(String[] args) {
        long start = System.nanoTime();

        long end = (System.nanoTime() - start) / 1_000_000;
        System.out.printf("물건 찾기 1 : %d msec%n", end);

    }
}
