package binance.stream.service.handler;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.marker.Markers;
import org.slf4j.Marker;
import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
public class GlobalMiniTickerHandler implements EventHandler {

    @Override
    public void handle(String stream, JsonElement data) {

        try {
            JsonArray ticker = data.getAsJsonArray();
            Marker marker = Markers.appendRaw("data", ticker.toString());
            log.info(marker, "GlobalMiniTicker", kv("stream", stream));

        } catch (Exception e) {
            log.error("Error processing global mini ticker message: {}", e.getMessage(), e);
        }

    }
}
