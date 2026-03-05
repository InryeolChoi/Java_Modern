package part19.example3;

import java.util.stream.IntStream;

public class CalculatePrime2 {
    public static void main(String[] args) {
        IntStream numbers = numbers();
        int head = head(numbers);
        IntStream filtered = tail(numbers).filter(n -> n % head != 0);
        primes(filtered).forEach(System.out::println);
    }

    static IntStream numbers() {
        return IntStream.iterate(2, n -> n + 1);
    }

    static int head(IntStream numbers) {
        return numbers.findFirst().getAsInt();
    }

    static IntStream tail(IntStream numbers) {
        return numbers.skip(1);
    }

    static IntStream primes(IntStream numbers) {
        int head = head(numbers);
        return IntStream.concat(
            IntStream.of(head),
            primes(tail(numbers).filter(n -> n % head != 0))
        );
    }
}
