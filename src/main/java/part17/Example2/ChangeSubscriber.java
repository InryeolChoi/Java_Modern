package part17.Example2;

import java.util.concurrent.Flow.*;

public class ChangeSubscriber implements Subscriber<PriceEvent> {

    private Subscription subscription;


    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(PriceEvent event) {
        // processor에게 받은 데이터를 출력
        System.out.printf("💰 현재가: %.2f | 변동률: %.4f%%%n",
                event.getPrice(),
                event.getChangePct());

        // 🔥 이상 데이터 조건이 생기면 -> cancel()을 가동한다.
        if (Math.abs(event.getChangePct()) > 90.0) {
            System.out.println("⚠ 이상 변동 감지 → cancel()");
            subscription.cancel();   // ⬅ 여기서 중단
            return;
        }
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {
        System.out.println("Stream 종료");
    }
}