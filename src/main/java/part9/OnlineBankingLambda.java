package part9;

import java.util.function.Consumer;

public class OnlineBankingLambda {
    // 더미 Customer 클래스
    static private class Customer {}

    // 더미 Database 클래스
    static private class Database {
        static Customer getCustomerWithId(int id) {
            return new Customer();
        }
    }

    // 비즈니스 로직
    public void processCustomer(int id, Consumer<Customer> makeCustomerHappy) {
        Customer c = Database.getCustomerWithId(id);
        makeCustomerHappy.accept(c);
    }

    // main
    public static void main(String[] args) {
        new OnlineBankingLambda().processCustomer(1337, (Customer c) -> System.out.println("Hello!"));
    }

}
