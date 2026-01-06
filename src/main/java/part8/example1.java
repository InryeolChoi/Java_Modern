package part8;

import java.util.*;

import static java.util.Map.entry;

public class example1 {
    public static void main(String[] args) {
        // of() 연습하기
        List<String> friends = List.of("Raphael", "Olivia", "Thibaut");
        Set<String> names = Set.of("Raphael", "Olivia", "Thibaut");

        // 10개 이하에서는 이렇게
        Map<String, Integer> map = Map.of("Raphael", 3, "Thibaut", 5);
        Map<String, String> restaurant = Map.ofEntries(
                entry("안성재", "모수"),
                entry("손종원", "이타닉가든"),
                entry("후덕죽", "팔선")
        );
    }
}
