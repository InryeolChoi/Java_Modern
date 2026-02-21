package part17.Example1;

import java.util.concurrent.Flow;

public class Main {
    public static void main(String[] args) {
        getTemp("Seoul").subscribe(new TempSubscriber());
    }

    private static Flow.Publisher<TempInfo> getTemp(String town) {
        return subscriber ->
                subscriber.onSubscribe(new TempSubscription(subscriber, town));
    }
}
