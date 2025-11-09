package binance.stream.service.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.marker.Markers;
import org.slf4j.Marker;

import java.time.Instant;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
public class TickerHandler implements EventHandler {

    @Override
    public void handle(String stream, JsonElement data) {

        try {
            JsonObject ticker = data.getAsJsonObject();
            Marker marker = Markers.appendRaw("data", ticker.toString());
            log.info(marker, "Ticker", kv("stream", stream));

        } catch (Exception e) {
            log.error("Error processing ticker message: {}", e.getMessage(), e);
        }

    }
}
