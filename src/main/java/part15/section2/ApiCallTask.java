package part15.section2;

import lombok.AllArgsConstructor;
import org.apache.hc.client5.http.fluent.Request;

import java.util.concurrent.Callable;

@AllArgsConstructor
public class ApiCallTask implements Callable<Boolean> {
    private final int id;

    @Override
    public Boolean call() throws Exception {
        String url = "https://jsonplaceholder.typicode.com/todos/" + id;

        String response = Request.get(url)
                .execute()
                .returnContent()
                .asString();

        // 계산 결과를 "값"으로 반환
        return response.contains("\"completed\": true");
    }
}
