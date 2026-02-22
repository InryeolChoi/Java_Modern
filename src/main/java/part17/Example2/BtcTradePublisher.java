package part17.Example2;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.Flow.*;

public class BtcTradePublisher implements Publisher<Double> {

    private final String wsUrl;
    private final HttpClient client = HttpClient.newHttpClient();
    private final SubmissionPublisher<Double> publisher = new SubmissionPublisher<>();

    private final ObjectMapper mapper = new ObjectMapper();
    private volatile WebSocket webSocket;

    public BtcTradePublisher(String wsUrl) {
        this.wsUrl = wsUrl;
    }

    // 시작
    public void start() {
        CompletableFuture<WebSocket> ws = client.newWebSocketBuilder()
            .buildAsync(URI.create(wsUrl),
                new WebSocket.Listener() {
                    private final StringBuilder buffer = new StringBuilder();

                    @Override
                    public void onOpen(WebSocket ws) {
                        BtcTradePublisher.this.webSocket = ws;
                        // backpressure: 다음 메시지 1개 요청
                        ws.request(1);
                    }

                    @Override
                    public CompletionStage<?> onText(WebSocket ws,
                                                     CharSequence data,
                                                     boolean last) {
                        // 메시지가 여러 조각으로 올 수 있어 buffer로 합침
                        buffer.append(data);
                        if (!last) {
                            ws.request(1);
                            return null;
                        }

                        String json = buffer.toString();
                        buffer.setLength(0);

                        // 가격 추출
                        Double price = extractTradePrice(json);
                        if (price != null && price > 0) {
                            // Flow로 발행 -> Processor로 감.
                            publisher.submit(price);
                        }

                        // 다음 메시지 1개 요청
                        ws.request(1);
                        return null;
                    }

                    @Override
                    public CompletionStage<?> onClose(WebSocket ws,
                                                      int statusCode,
                                                      String reason) {
                        publisher.close();
                        return null;
                    }

                    @Override
                    public void onError(WebSocket ws, Throwable error) {
                        error.printStackTrace();
                        publisher.closeExceptionally(error);
                    }
                }
            );

        // 연결 완료까지 대기 (여기만 동기)
        ws.join();
    }

    private Double extractTradePrice(String json) {
        try {
            // Binance trade payload 예: {"e":"trade", ... , "p":"67982.34", ...}
            JsonNode root = mapper.readTree(json);
            JsonNode pNode = root.get("p");
            if (pNode == null) return null;
            return Double.parseDouble(pNode.asText());
        } catch (Exception e) {
            // 파싱 실패는 스트림 전체를 죽이지 말고 무시
            return null;
        }
    }


    public void stop() {
        WebSocket ws = this.webSocket;
        if (ws != null) {
            try {
                ws.sendClose(WebSocket.NORMAL_CLOSURE, "bye").join();
            } catch (Exception ignored) {}
        }
        publisher.close();
    }


    @Override
    public void subscribe(Subscriber<? super Double> subscriber) {
        publisher.subscribe(subscriber);
    }
}