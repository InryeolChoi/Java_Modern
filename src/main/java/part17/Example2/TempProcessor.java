package part17.Example2;

import part17.Example1.TempInfo;

import java.util.concurrent.Flow.*;

public class TempProcessor implements Processor<TempInfo, TempInfo> {

    private Subscriber<? super TempInfo> subscriber;

    @Override
    public void subscribe(Subscriber<? super TempInfo> subscriber) {
        this.subscriber = subscriber;
    }

    @Override
    public void onNext(TempInfo t){
        subscriber.onNext(new TempInfo(t.getTown(),
                (t.getTemp() - 32) * 5 / 9));
    }

    @Override
    public void onSubscribe(Subscription s){
        subscriber.onSubscribe(s);
    }

    @Override
    public void onError(Throwable e){
        subscriber.onError(e);
    }

    @Override
    public void onComplete(){
        subscriber.onComplete();
    }
}
