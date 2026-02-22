package part17.Example2;

import java.util.concurrent.Flow.*;
import java.util.concurrent.SubmissionPublisher;

public class PriceChangeProcessor extends SubmissionPublisher<PriceEvent>
        implements Processor<Double, PriceEvent> {

    private Subscription subscription;
    private Double prev = null;

    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(Double price) {
        // 변동률 계산
        if (price != null && price > 0 && prev != null && prev > 0) {
            double changePct = ((price - prev) / prev) * 100.0;

            // PriceEvent를 통해 가격와 변동률을 정리
            PriceEvent event = new PriceEvent(price, changePct);

            // subscriber에게 넘기는 과정
            submit(event);
        }

        prev = price;
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        closeExceptionally(throwable);
    }

    @Override
    public void onComplete() {
        close();
    }
}
