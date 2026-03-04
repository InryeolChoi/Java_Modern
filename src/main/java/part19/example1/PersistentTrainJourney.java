package part19.example1;

import java.util.function.Consumer;

public class PersistentTrainJourney {
    // Destructive update(파괴적 갱신)
    static TrainJourney link(TrainJourney a, TrainJourney b){
        if (a == null)
            return b;
        TrainJourney t = a;
        while (t.onward != null) {
            t = t.onward;
        }
        t.onward = b;
        return a;
    }

    // Persistent structure(영속 자료구조)
    static TrainJourney append(TrainJourney a, TrainJourney b){
        return a == null ? b : new TrainJourney(a.price, append(a.onward, b));
    }

    // 순회 함수 : 연결 리스트를 재귀로 출력
    static void visit(TrainJourney journey, Consumer<TrainJourney> c) {
        if (journey != null) {
            c.accept(journey);
            visit(journey.onward, c);
        }
    }

    // main
    public static void main(String[] args) {
        TrainJourney tj1 = new TrainJourney(40, new TrainJourney(30, null));
        TrainJourney tj2 = new TrainJourney(20, new TrainJourney(50, null));

        System.out.println("======================");

        /* append 실행 : 새 리스트를 만듬 */
        TrainJourney appended = append(tj1, tj2);
        System.out.println("appended:");
        visit(appended, tj -> {
            System.out.print(tj.price + " - ");
        });
        System.out.println();

        TrainJourney appended2 = append(tj1, tj2);
        System.out.println("appended2:");
        visit(appended2, tj -> {
            System.out.print(tj.price + " - ");
        });
        System.out.println("\n");

        // appended와 appended2는 같은 개체인가?
        System.out.println("appended 의 주소값 : " +
                Integer.toHexString(System.identityHashCode(appended)));
        System.out.println("appended2 의 주소값 : " +
                Integer.toHexString(System.identityHashCode(appended2)) + "\n");

        if (appended == appended2) {
            System.out.println("appended와 appended2는 같은 개체");
        }
        else {
            System.out.println("appended와 appended2는 다른 개체");
        }

        System.out.println("======================");

        /* link 실행 : link는 기존 리스트를 수정한다. */
        TrainJourney linked = link(tj1, tj2);
        visit(linked, tj -> {
            System.out.print(tj.price + " - ");
        });
        System.out.println();

        // ... 여기서 이 코드의 주석을 해제하면 tj2가 이미 바뀐 tj1의 끝에 추가된다.
        // 끝없는 visit() 재귀 호출이 일어나면서 StackOverflowError가 발생한다.
        // TrainJourney linked2 = link(tj1, tj2);
        // visit(linked2, tj -> { System.out.print(tj.price + " - "); });
        // System.out.println();

        System.out.println("======================");
        compareLinkAndAppend();
    }

    // 링크 비교
    private static void compareLinkAndAppend() {
        System.out.println("Destructive update:");
        TrainJourney firstJourney = new TrainJourney(1, null);
        TrainJourney secondJourney = new TrainJourney(2, null);
        TrainJourney xToZ = link(firstJourney, secondJourney);
        System.out.printf("firstJourney (X to Y) = %s%n", firstJourney);
        System.out.printf("secondJourney (Y to Z) = %s%n", secondJourney);
        System.out.printf("X to Z = %s%n", xToZ);

        System.out.println();
        System.out.println("The functional way:");
        firstJourney = new TrainJourney(1, null);
        secondJourney = new TrainJourney(2, null);
        xToZ = append(firstJourney, secondJourney);
        System.out.printf("firstJourney (X to Y) = %s%n", firstJourney);
        System.out.printf("secondJourney (Y to Z) = %s%n", secondJourney);
        System.out.printf("X to Z = %s%n", xToZ);
    }
}
