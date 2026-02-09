package part15.section1;

import org.apache.hc.client5.http.fluent.Request;

import java.io.IOException;
import java.util.concurrent.Semaphore;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ApiTask implements Runnable {

    private final int id;
    private final ResultCounter counter;
    private final Semaphore semaphore;

    // 실제 1개의 쓰레드가 하는 동작
    @Override
    public void run() {
        try {
            semaphore.acquire(); // 동시 요청 제한

            String url = "https://jsonplaceholder.typicode.com/todos/" + id;
            String response = Request.get(url)
                    .execute()
                    .returnContent()
                    .asString();

            counter.addSuccess();

            if (response.contains("\"completed\": true")) {
                counter.addCompleted();
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("요청 실패: " + id);
        } finally {
            semaphore.release();
        }
    }
}