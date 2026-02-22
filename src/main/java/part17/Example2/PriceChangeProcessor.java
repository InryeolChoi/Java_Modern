package part17.Example2;

import java.util.concurrent.Flow.*;
import java.util.concurrent.SubmissionPublisher;

public class PriceChangeProcessor extends SubmissionPublisher<PriceEvent>
        implements Processor<Double, PriceEvent> {

    // 변수 선언
    private Subscription upstream;
    private Double prev = null;

    /**
     * PriceChangeProcessor의 publisher에 해당하는 메소드
     *
     */

    @Override
    public void onSubscribe(Subscription subscription) {
        this.upstream = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(Double price) {
        try {
            if (price != null && price > 0 && prev != null && prev > 0) {
                double changePct = ((price - prev) / prev) * 100.0;
                submit(new PriceEvent(price, changePct));
            }
            prev = price;
        } finally {
            // 다음 데이터 1개 더 요청 (backpressure)
            if (upstream != null) upstream.request(1);
        }
    }

    // ✅ upstream cancel 전파
    public void cancelUpstream() {
        Subscription s = this.upstream;
        if (s != null) {
            System.out.println("🧨 downstream cancel 감지 → upstream cancel 전파");
            s.cancel();
        }
        // Processor 자체도 닫아주는 게 안전함 (아래 구독자들에게 complete 전파)
        close();
    }

    @Override
    public void onError(Throwable throwable) {
        closeExceptionally(throwable);
    }

    @Override
    public void onComplete() {
        close();
    }

    /**
     * PriceChangeProcessor의 subscriber에 해당하는 메소드
     * 이렇게 해야 ChangeSubsciber에서 오는 cancel()을 잡기 편하다.
     */
    @Override
    public void subscribe(Subscriber<? super PriceEvent> subscriber) {
        super.subscribe(
            new Subscriber<PriceEvent>() {
                @Override
                public void onSubscribe(Subscription downstreamSub) {
                    // downstreamSub를 감싸서 cancel을 가로챈다
                    Subscription wrapped = new Subscription() {
                        @Override
                        public void request(long n) {
                            downstreamSub.request(n);
                        }

                        @Override
                        public void cancel() {
                            // 1) 구독 해제
                            downstreamSub.cancel();

                            // 2) 위쪽 publisher에게 cancel 전파
                            cancelUpstream();
                        }
                    };

                    // 구독자에게는 "wrapped subscription"을 넘긴다
                    subscriber.onSubscribe(wrapped);
                }

                @Override
                public void onNext(PriceEvent item) {
                    subscriber.onNext(item);
                }

                @Override
                public void onError(Throwable throwable) {
                    subscriber.onError(throwable);
                }

                @Override
                public void onComplete() {
                    subscriber.onComplete();
                }
            }
        );
    }

}
