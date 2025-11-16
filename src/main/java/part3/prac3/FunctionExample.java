package part3.prac3;

import java.util.function.Function;

public class FunctionExample {
    public static void main(String[] args) {
        // String → Integer
        Function<String, Integer> stringLength = s -> s.length();

        int length = stringLength.apply("Modern Java");
        System.out.println("문자열 길이: " + length);
    }
}
