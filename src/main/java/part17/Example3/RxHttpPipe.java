package part17.Example3;

import com.sun.net.httpserver.HttpExchange;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class RxHttpPipe {

    // 플레이리스트용: 짧으니 그냥 한 번에 가져오기
    public static byte[] getBytesBlocking(String url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setInstanceFollowRedirects(true);
        conn.setConnectTimeout(10_000);
        conn.setReadTimeout(10_000);
        conn.setRequestProperty("User-Agent", "rxjava-hls-proxy/1.0");

        try (InputStream in = conn.getInputStream();
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buf = new byte[16 * 1024];
            int n;
            while ((n = in.read(buf)) >= 0) bos.write(buf, 0, n);
            return bos.toByteArray();
        } finally {
            conn.disconnect();
        }
    }

    // 세그먼트(ts)용: 길고 큰 데이터 → Rx로 청크 스트리밍
    public static void pipeStreaming(HttpExchange ex, String upstreamUrl) throws IOException {

        HttpURLConnection conn = (HttpURLConnection) new URL(upstreamUrl).openConnection();
        conn.setInstanceFollowRedirects(true);
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);
        conn.setRequestProperty("User-Agent", "rxjava-hls-proxy/1.0");

        InputStream upstream = conn.getInputStream();
        OutputStream client = ex.getResponseBody();

        try {
            byte[] buffer = new byte[16 * 1024];
            int n;

            while ((n = upstream.read(buffer)) != -1) {
                client.write(buffer, 0, n);
            }

        } catch (IOException e) {
            System.out.println("⚠ client aborted segment (normal in HLS)");
        } finally {
            try { upstream.close(); } catch (Exception ignored) {}
            try { client.close(); } catch (Exception ignored) {}
            conn.disconnect();
        }
    }
}