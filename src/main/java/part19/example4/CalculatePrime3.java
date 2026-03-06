package part19.example4;

public class CalculatePrime3 {
    public static void main(String[] args) {
        // LazyList 미사용
        System.out.println("------ LazyList 미사용 ------");
        MyList<Integer> list = new MyLinkedList<>(5,
                new MyLinkedList<>(10, new Empty<>()));
        System.out.println(list.head());

        // LazyList 사용
        System.out.println("------ LazyList 사용 ------");
        LazyList<Integer> numbers = from(2);
        int two = numbers.head();
        int three = numbers.tail().head();
        int four = numbers.tail().tail().head();
        System.out.println(two + " " + three + " " + four);

        // LazyList 응용 : 무한히 소수 생성
        // stackoverflow 발생
        // printAll(primes(from(2)));
    }

    static LazyList<Integer> from(int n) {
        return new LazyList<Integer>(n, () -> from(n + 1));
    }

    static MyList<Integer> primes(MyList<Integer> numbers) {
        return new LazyList<>(numbers.head(),
            () -> primes(numbers.tail().filter(n -> n % numbers.head() != 0)));
    }

    static <T> void printAll(MyList<T> list) {
        if (list.isEmpty())
            return;
        System.out.println(list.head());
        printAll(list.tail());
    }
}
