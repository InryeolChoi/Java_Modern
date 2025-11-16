package part3.prac3;

import java.util.*;
import java.util.function.Consumer;

public class ConsumerExample {
    public static void main(String[] args) {
        Consumer<String> printer = s -> System.out.println("출력: " + s);
        printer.accept("Hello Consumer!");
    }
}
