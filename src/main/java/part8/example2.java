package part8;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class example2 {
    public static void main(String[] args) {
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

        // removeif로 Emma 제거하기
        names.removeIf(n -> n.startsWith("E"));
        System.out.println("2차 제거\n" + names);

    }
}
