package part17.Example3;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class ProxyServer {

    // ✅ 원본 HLS (샘플) - 이게 “진짜 영상 데이터”가 있는 곳
    private static final String UPSTREAM_PLAYLIST =
            "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8";

    private final int port;

    public ProxyServer(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/", this::handleIndex);

        // ✅ 우리가 브라우저에 줄 플레이리스트(= m3u8)
        server.createContext("/hls/playlist.m3u8", this::handlePlaylist);

        server.createContext("/hls/playlist", this::handleVariantPlaylist);
        // ✅ 세그먼트(ts) 프록시: /hls/seg?u=<encoded>
        server.createContext("/hls/seg", this::handleSegment);

        server.start();
    }

    private void handleIndex(HttpExchange ex) throws IOException {
        String html = """
                <html>
                <head>
                  <meta charset="UTF-8">
                  <title>HLS Proxy Streaming</title>
                </head>
                <body style="background:black;color:white;text-align:center;">
                  <h1>🌍 HLS 실시간 스트리밍 (프록시)</h1>
                
                  <video id="video" width="1000" controls autoplay muted></video>
                
                  <script>
                    const video = document.getElementById('video');
                    const url = '/hls/playlist.m3u8';
                
                    if (video.canPlayType('application/vnd.apple.mpegurl')) {
                      // Safari
                      video.src = url;
                    } else if (Hls.isSupported()) {
                      // Chrome, Edge
                      const hls = new Hls();
                      hls.loadSource(url);
                      hls.attachMedia(video);
                    } else {
                      alert("HLS not supported");
                    }
                  </script>
                </body>
                </html>
                """;

        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        ex.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(bytes);
        }
    }

    private void handlePlaylist(HttpExchange ex) throws IOException {
        // 원본 m3u8을 가져와서 "세그먼트 URL"들을 전부 localhost 프록시로 바꿔치기
        byte[] upstream = RxHttpPipe.getBytesBlocking(UPSTREAM_PLAYLIST);

        String rewritten = HlsRewrite.rewriteM3u8(
                new String(upstream, StandardCharsets.UTF_8),
                UPSTREAM_PLAYLIST
        );

        byte[] bytes = rewritten.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "application/x-mpegURL; charset=UTF-8");
        // 플레이리스트는 보통 짧으니까 length 보내도 OK
        ex.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(bytes);
        }
    }

    private void handleSegment(HttpExchange ex) throws IOException {
        String rawQuery = ex.getRequestURI().getRawQuery(); // u=...
        String u = null;
        if (rawQuery != null) {
            for (String kv : rawQuery.split("&")) {
                int idx = kv.indexOf('=');
                if (idx > 0 && "u".equals(kv.substring(0, idx))) {
                    u = URLDecoder.decode(kv.substring(idx + 1), StandardCharsets.UTF_8);
                    break;
                }
            }
        }

        if (u == null || u.isBlank()) {
            byte[] msg = "missing query param u".getBytes(StandardCharsets.UTF_8);
            ex.sendResponseHeaders(400, msg.length);
            try (OutputStream os = ex.getResponseBody()) { os.write(msg); }
            return;
        }

        // 세그먼트는 커서 length 모름 → chunked로 스트리밍
        ex.getResponseHeaders().set("Content-Type", "video/MP2T");
        ex.sendResponseHeaders(200, 0);

        // ✅ RxJava로 upstream 바이트를 청크 단위로 읽어다가 client로 바로 흘려보내기
        RxHttpPipe.pipeStreaming(ex, u);
    }

    private void handleVariantPlaylist(HttpExchange ex) throws IOException {
        String rawQuery = ex.getRequestURI().getRawQuery();
        String u = URLDecoder.decode(rawQuery.substring(2), StandardCharsets.UTF_8);

        byte[] upstream = RxHttpPipe.getBytesBlocking(u);

        String rewritten = HlsRewrite.rewriteM3u8(
                new String(upstream, StandardCharsets.UTF_8),
                u
        );

        byte[] bytes = rewritten.getBytes(StandardCharsets.UTF_8);

        ex.getResponseHeaders().set("Content-Type", "application/x-mpegURL");
        ex.sendResponseHeaders(200, bytes.length);

        try (OutputStream os = ex.getResponseBody()) {
            os.write(bytes);
        }
    }
}