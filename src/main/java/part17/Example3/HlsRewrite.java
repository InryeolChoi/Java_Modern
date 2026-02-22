package part17.Example3;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class HlsRewrite {

    public static String rewriteM3u8(String m3u8Text, String upstreamPlaylistUrl) {
        URI base = URI.create(upstreamPlaylistUrl);
        StringBuilder out = new StringBuilder();

        for (String line : m3u8Text.split("\\r?\\n")) {

            if (line.isBlank() || line.startsWith("#")) {
                out.append(line).append("\n");
                continue;
            }

            URI abs = base.resolve(line.trim());
            String encoded = URLEncoder.encode(abs.toString(), StandardCharsets.UTF_8);

            if (line.endsWith(".m3u8")) {
                // 🔥 variant playlist
                out.append("/hls/playlist?u=").append(encoded).append("\n");
            } else {
                // 🔥 segment (.ts)
                out.append("/hls/seg?u=").append(encoded).append("\n");
            }
        }
        return out.toString();
    }
}