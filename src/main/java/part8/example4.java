package part8;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class example4 {
    public static void main(String[] args) {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

        map.put("apple", 100);
        map.put("banana", 200);
        map.put("orange", 150);
        map.put("grape", 300);

        /* =====================================================
         * 1) mappingCount()
         * ===================================================== */
        System.out.println("1) mappingCount()");
        System.out.println("mappingCount = " + map.mappingCount());
        // size() 대신 사용하는 "대략적인 엔트리 수"
        // 동시성 환경에서 더 잘 스케일됨

        /* =====================================================
         * 2) forEach() - 병렬 순회
         * ===================================================== */
        System.out.println("\n2) forEach()");

        map.forEach(1, (k, v) -> {
            System.out.println(
                    Thread.currentThread().getName() +
                            " -> " + k + " = " + v
            );
        });

        // threshold = 1
        // → 엔트리가 충분하면 병렬 실행
        // → 출력 순서가 섞이는 걸 확인 가능

        /* =====================================================
         * 3) search() - 병렬 탐색
         * ===================================================== */
        System.out.println("\n3) search()");

        String expensiveFruit = map.search(1, (k, v) -> {
            if (v >= 250) {
                return k; // 조건 만족 시 즉시 반환
            }
            return null;
        });

        System.out.println("search result = " + expensiveFruit);

        // 여러 스레드가 동시에 탐색
        // 가장 먼저 조건을 만족한 결과 하나만 반환
        // 순서 비결정적

        /* =====================================================
         * 4) reduce() - 병렬 집계
         * ===================================================== */
        System.out.println("\n4) reduce()");

        Integer totalPrice = map.reduce(
                1,
                (k, v) -> v,          // (K, V) → 값으로 변환
                Integer::sum          // 값들을 합침
        );

        System.out.println("totalPrice = " + totalPrice);

        // Map 전용 병렬 reduce
        // stream + parallel 없이도 병렬 집계 가능

        /* =====================================================
         * 5) keySet() - live view
         * ===================================================== */
        System.out.println("\n5) keySet()");
        Set<String> keys = map.keySet();

        System.out.println("keys before = " + keys);
        // keySet에서 제거 → map에서도 제거
        keys.remove("banana");

        System.out.println("keys after = " + keys);
        System.out.println("map after keySet.remove = " + map);

        /* =====================================================
         * 6) weakly consistent 확인
         * ===================================================== */
        System.out.println("\n6) weakly consistent iteration");

        for (String key : map.keySet()) {
            System.out.println("iterating key = " + key);

            // 순회 중 구조 변경
            map.put("melon", 400);
        }

        System.out.println("final map = " + map);

        // 예외 없음
        // 변경 내용이 순회 중 보일 수도 있고 안 보일 수도 있음

    }
}
