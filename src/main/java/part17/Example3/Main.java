package part17.Example3;

public class Main {
    public static void main(String[] args) throws Exception {
        new ProxyServer(8080).start();
        System.out.println("🌍 Open: http://localhost:8080");
    }
}