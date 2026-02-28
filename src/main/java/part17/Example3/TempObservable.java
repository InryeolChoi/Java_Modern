package part17.Example3;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
public class TempObservable {

    // “1초마다 town의 온도를 발행하는 Observable”을 만든다.
    public static Observable<TempInfo> getTemperature(String town) {
        return Observable.create(emitter ->
            Observable.interval(1, TimeUnit.SECONDS)
                .subscribe(i -> {
                    if (!emitter.isDisposed()) {
                        if (i >= 5) {emitter.onComplete();}
                        else {
                            try {emitter.onNext(TempInfo.fetch(town));}
                            catch (Exception e) {emitter.onError(e);}
                        }
                    }
                }
            )
        );
    }

    /* getCelsiusTemperature */
    // map을 이용해서 Flow API로 치면 Processor를 구현해야 했을 작업을 한 줄로 끝냄.
    public static Observable<TempInfo> getCelsiusTemperature(String town) {
        return getTemperature(town).map(temp ->
                new TempInfo(temp.getTown(), (temp.getTemp() - 32) * 5 / 9));
    }

    /* getCelsiusTemperatures */
    // 여러 개의 Observable을 하나로 합친다.
    // 각 도시마다 Observable 생성 → 리스트로 만듦 → merge.
    public static Observable<TempInfo> getCelsiusTemperatures(String... towns) {
        return Observable.merge(Arrays.stream(towns)
                .map(TempObservable::getCelsiusTemperature)
                .collect(toList()));
    }

}
