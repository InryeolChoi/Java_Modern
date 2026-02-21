package part17.Example1;

import java.util.concurrent.Flow.*;

public class TempSubscriber implements Subscriber<TempInfo> {
    private Subscription subscription;

    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(TempInfo value) {
        System.out.println(value);
        subscription.request(1);
    }

    @Override
    public void onError(Throwable t) {
        System.err.println("Error: " + t.getMessage());
    }

    @Override
    public void onComplete() {
        System.out.println("Done!");
    }
}
