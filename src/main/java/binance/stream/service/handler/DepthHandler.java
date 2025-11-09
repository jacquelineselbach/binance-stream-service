package binance.stream.service.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.marker.Markers;
import org.slf4j.Marker;
import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
public class DepthHandler implements EventHandler {

    @Override
    public void handle(String stream, JsonElement data) {

        try {
            JsonObject depth = data.getAsJsonObject();
            Marker marker = Markers.appendRaw("data", depth.toString());
            log.info(marker, "Depth", kv("stream", stream));

        } catch (Exception e) {
            log.error("Error processing depth message: {}", e.getMessage(), e);
        }

    }
}
