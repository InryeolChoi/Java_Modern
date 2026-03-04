package part19.example2;

public class PersistentTree {
    static class Tree {
        private String key;
        private int val;
        private Tree left, right;

        public Tree(String k, int v, Tree l, Tree r) {
            key = k;
            val = v;
            left = l;
            right = r;
        }
    }

    // update() → 기존 트리를 변경 (mutable)
    public static Tree update(String k, int newval, Tree t) {
        if (t == null) {
            t = new Tree(k, newval, null, null);
        }
        else if (k.equals(t.key)) {
            t.val = newval;
        }
        else if (k.compareTo(t.key) < 0) {
            t.left = update(k, newval, t.left);
        }
        else {
            t.right = update(k, newval, t.right);
        }
        return t;
    }

    // fupdate() → 새 트리를 생성 (persistent)
    public static Tree fupdate(String k, int newval, Tree t) {
        return (t == null) ?
            new Tree(k, newval, null, null) :
                k.equals(t.key) ?
                    new Tree(k, newval, t.left, t.right) :
                        k.compareTo(t.key) < 0 ?
                            new Tree(t.key, t.val, fupdate(k,newval, t.left), t.right) :
                            new Tree(t.key, t.val, t.left, fupdate(k,newval, t.right));
    }

    // lookup() : key → value 찾기
    // 탐색용 함수
    public static int lookup(String k, int defaultval, Tree t) {
        if (t == null)
            return defaultval;
        if (k.equals(t.key))
            return t.val;
        return lookup(k, defaultval, k.compareTo(t.key) < 0 ? t.left : t.right);
    }

    // main()
    public static void main(String[] args) {
        // 초기 이진 탐색 트리 생성
        //
        //            Mary:22
        //           /       \
        //      Emily:20     Tian:29
        //      /     \       /
        // Alan:50 Georgie:23 Raoul:23
        //
        Tree t = new Tree("Mary", 22,
                new Tree("Emily", 20,
                    new Tree("Alan", 50, null, null),
                    new Tree("Georgie", 23, null, null)
                ),
                new Tree("Tian", 29,
                    new Tree("Raoul", 23, null, null),
                    null
                )
        );

        // 기존 트리 t에서 "Raoul" 검색
        // 존재하므로 값 23 반환
        System.out.printf("Raoul: %d%n", lookup("Raoul", -1, t));

        // 기존 트리 t에서 "Jeff" 검색
        // 존재하지 않으므로 default 값 -1 반환
        System.out.printf("Jeff: %d%n", lookup("Jeff", -1, t));


        // -------- Persistent update (함수형 방식) --------
        // fupdate()는 기존 트리를 수정하지 않고
        // 새로운 트리를 생성하여 반환한다.
        //
        // 기존 트리 t는 그대로 유지된다.
        Tree f = fupdate("Jeff", 80, t);

        // f 트리에는 Jeff 노드가 추가되었으므로
        // Jeff 검색 시 80 반환
        System.out.printf("Jeff: %d%n", lookup("Jeff", -1, f));


        // -------- Mutable update (기존 방식) --------
        // update()는 기존 트리 t를 직접 수정한다.
        //
        // 즉, t 자체에 Jim 노드가 추가된다.
        Tree u = update("Jim", 40, t);

        // Jeff는 f 트리에만 존재하고
        // t는 수정되지 않았으므로 Jeff는 여전히 없음
        // → default 값 -1 반환
        System.out.printf("Jeff: %d%n", lookup("Jeff", -1, u));

        // update()로 t에 Jim이 추가되었으므로
        // Jim 검색 시 40 반환
        System.out.printf("Jim: %d%n", lookup("Jim", -1, u));


        // -------- 다시 Persistent update --------
        // fupdate()를 다시 실행하지만,
        // 이번에는 update()로 이미 수정된 t를 기반으로 한다.
        //
        // 즉, t에는 이미 Jim 노드가 존재한다.
        Tree f2 = fupdate("Jeff", 80, t);

        // Jeff는 새로 추가되었으므로 80 반환
        System.out.printf("Jeff: %d%n", lookup("Jeff", -1, f2));

        // f2는 수정된 t를 기반으로 만들어졌으므로
        // Jim 노드도 그대로 포함된다.
        // → Jim 검색 시 40 반환
        System.out.printf("Jim: %d%n", lookup("Jim", -1, f2));
    }

}
