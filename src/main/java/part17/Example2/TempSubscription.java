package part17.Example2;

import lombok.AllArgsConstructor;

import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

@AllArgsConstructor
public class TempSubscription implements Subscription {

    private final Subscriber<? super TempInfo> subscriber;
    private final String town;

    @Override
    public void request(long n) {
        for (long i = 0; i < n; i++) {
            try {
                subscriber.onNext (TempInfo.fetch(town));
            } catch (Exception e) {
                subscriber.onError(e);
                break;
            }
        }
    }

    @Override
    public void cancel() {
        subscriber.onComplete();
    }
}
