package part8;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class example2 {
    public static void main(String[] args) {
        /* removeIf() */
        System.out.println("removeIf() 예시");
        List<String> names = new ArrayList<>();
        names.add("Alice");
        names.add("Bob");
        names.add("Carl");
        names.add("David");
        names.add("Emma");

        // iterator로 Alice 제거하기
        Iterator<String> it = names.iterator();
        while (it.hasNext()) {
            if (it.next().startsWith("A")) {
                it.remove();
            }
        }
        System.out.println("1차 제거\n" + names);

        // removeIf()로 Emma 제거하기
        names.removeIf(n -> n.startsWith("E"));
        System.out.println("2차 제거\n" + names);

        /* removeAll() */
        System.out.println("removeAll() 예시");
        List<String> words = new ArrayList<>();
        words.add("java");
        words.add("lambda");

        words.replaceAll(String::toUpperCase);
        System.out.println(words);
    }
}
