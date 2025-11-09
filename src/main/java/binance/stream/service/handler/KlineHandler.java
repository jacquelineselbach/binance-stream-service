package binance.stream.service.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.marker.Markers;
import org.slf4j.Marker;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
public class KlineHandler implements EventHandler {

    @Override
    public void handle(String stream, JsonElement data) {

        try {
            JsonObject value = data.getAsJsonObject();
            JsonObject kline = value.has("k") ? value.getAsJsonObject("k") : value;

            Marker marker = Markers.appendRaw("data", kline.toString());
            log.info(marker, "Kline1m", kv("stream", stream));

        } catch (Exception e) {
            log.error("Error processing kline message: {}", e.getMessage(), e);
        }

    }
}
