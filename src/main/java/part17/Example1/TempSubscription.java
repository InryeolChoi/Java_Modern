package part17.Example1;

import lombok.AllArgsConstructor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow.*;

@AllArgsConstructor
public class TempSubscription implements Subscription {

    private final Subscriber<? super TempInfo> subscriber;
    private final String town;

//    private final ExecutorService e = Executors.newSingleThreadExecutor();

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


