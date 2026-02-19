package part16.Example3;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Random;

public class Util {
    private static final Random RANDOM = new Random(0);

    public static void delay() {
        int delay = 500;
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static final ThreadLocal<DecimalFormat> formatter =
            ThreadLocal.withInitial(() -> new DecimalFormat("#.##"));

    public static double format(double number) {
        return Double.parseDouble(formatter.get().format(number));
    }

}