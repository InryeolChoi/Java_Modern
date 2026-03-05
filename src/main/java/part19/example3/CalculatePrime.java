package part19.example3;

public class CalculatePrime {
    public static void main(String[] args) {
        MyMathUtil util = new MyMathUtil();

        util.primes(10).forEach(System.out::println);
    }
}
