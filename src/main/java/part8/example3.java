package part8;

import java.util.*;

public class example3 {
    public static void main(String[] args) {
        /* =====================================================
         * 1) forEach
         * ===================================================== */
        System.out.println("1) foreach");

        Map<String, Integer> scores = new HashMap<>();
        scores.put("Alice", 90);
        scores.put("Bob", 85);
        scores.put("Charlie", 95);

        scores.forEach((name, score) ->
                System.out.println(name + " : " + score)
        );

        /* =====================================================
         * 2) entry.comparingByKey / comparingByValue
         * ===================================================== */
        System.out.println("\n2) comparingByKey / comparingByValue");

        scores.entrySet().stream().sorted(Map.Entry.comparingByKey())
                .forEach(e -> System.out.println("key 정렬 " + e));

        scores.entrySet().stream().sorted(Map.Entry.comparingByValue())
                .forEach(e -> System.out.println("key 정렬 " + e));

        /* =====================================================
         * 3) getOrDefault
         * ===================================================== */
        System.out.println("\n3) getOrDefault");

        int davidScore = scores.getOrDefault("David", 0);
        System.out.println(davidScore);

        /* =====================================================
         * 4) computeIfAbsent / computeIfPresent / compute
         * ===================================================== */
        System.out.println("\n4) compute 계열");
        Map<String, List<String>> group = new HashMap<>();

        // computeIfAbsent: 없을 때만 생성
        group.computeIfAbsent("fruit", k -> new ArrayList<>())
                .add("apple");
        group.computeIfAbsent("fruit", k -> new ArrayList<>())
                .add("banana");
        System.out.println("computeIfAbsent: " + group);

        // computeIfPresent: 있을 때만 수정
        Map<String, Integer> counts = new HashMap<>();
        counts.put("apple", 2);
        counts.computeIfPresent("apple", (k, v) -> v + 1);
        counts.computeIfPresent("banana", (k, v) -> v + 1); // 실행 안 됨
        System.out.println("computeIfPresent: " + counts);

        // compute: 항상 호출
        counts.compute("banana", (k, v) -> v == null ? 1 : v + 1);
        counts.compute("apple", (k, v) -> v == null ? 1 : v + 1);
        System.out.println("compute: " + counts);

        /* =====================================================
         * 5) remove 오버로드
         * ===================================================== */
        System.out.println("\n5) remove 오버로드");

        Map<String, Integer> map = new HashMap<>();
        map.put("A", 1);
        map.put("B", 2);

        map.remove("A", 2); // 값 다름 → 제거 안 됨
        System.out.println("after remove(A,2): " + map);

        map.remove("A", 1); // 키+값 일치 → 제거
        System.out.println("after remove(A,1): " + map);

        /* =====================================================
         * 6) replace / replaceAll
         * ===================================================== */
        System.out.println("\n6) replace");

        Map<String, Integer> prices = new HashMap<>();
        prices.put("apple", 1000);
        prices.put("banana", 1500);

        prices.replace("apple", 1000, 1200); // 조건부 변경
        prices.replace("banana", 2000);      // 키 있으면 변경

        prices.replaceAll((k, v) -> v + 100); // 전체 변경
        System.out.println("replace 결과: " + prices);

        /* =====================================================
         * 7) putAll
         * ===================================================== */
        System.out.println("\n7) putAll");

        Map<String, Integer> extra = new HashMap<>();
        extra.put("orange", 1800);
        extra.put("banana", 2000); // 기존 값 덮어씀

        prices.putAll(extra); // price에 extra를 더함.
        System.out.println("putAll 결과: " + prices);

    }
}
