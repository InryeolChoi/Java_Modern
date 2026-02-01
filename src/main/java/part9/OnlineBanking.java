package part9;

abstract class OnlineBanking {
    // 더미 Customer 클래스
    static private class Customer {}

    // 더미 Database 클래스
    static private class Database {
        static Customer getCustomerWithId(int id) {
            return new Customer();
        }
    }

    // makeCustomerHappy()
    abstract void makeCustomerHappy(Customer c);

    // 비즈니스 로직 정의
    public void processCustomer(int id) {
        Customer c = Database.getCustomerWithId(id);
        makeCustomerHappy(c);
    }

}
