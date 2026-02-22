package part17.Example2;

import java.util.concurrent.CountDownLatch;

public class Main {
    // URI
    public static final String URI = "wss://stream.binance.com:9443/ws/btcusdt@trade";

    public static void main(String[] args) throws Exception {

        // 선언
        BtcTradePublisher publisher = new BtcTradePublisher(URI);
        PriceChangeProcessor processor = new PriceChangeProcessor();
        ChangeSubscriber subscriber = new ChangeSubscriber();

        // 1) WebSocket → Publisher : BTC/USDT 실시간 trade 스트림
        // 2) Publisher -> Processor : 가격 → 변동률(%)로 변환
        // 3) Processor -> Subscriber : 출력

        publisher.subscribe(processor);
        processor.subscribe(subscriber);

        // 🔥 종료 훅 등록
        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> {
                    System.out.println("\n🛑 Shutdown 감지 → 정상 종료 시도");
                    publisher.stop();
                })
        );

        // publisher 시작
        publisher.start();

        // publisher가 무한히 살도록 하는 로직
        new CountDownLatch(1).await();
    }
}
