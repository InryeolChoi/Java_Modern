package part17.Example3;

import io.reactivex.rxjava3.core.Observable;

import static part17.Example3.TempObservable.getCelsiusTemperatures;

public class Main {

    public static void main(String[] args) {
        Observable<TempInfo> observable = getCelsiusTemperatures("New York", "Chicago", "San Francisco");
        observable.subscribe(new TempObserver());

        try {
            Thread.sleep(10000L);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
