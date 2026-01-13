package part8;

import java.util.*;

public class example3 {
    public static void main(String[] args) {
        List<String> words = new ArrayList<>();
        words.add("java");
        words.add("lambda");

        words.replaceAll(String::toUpperCase);
        System.out.println(words);
    }
}
