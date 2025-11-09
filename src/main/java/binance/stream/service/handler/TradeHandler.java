package binance.stream.service.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.marker.Markers;
import org.slf4j.Marker;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
public class TradeHandler implements EventHandler {

    @Override
    public void handle(String stream, JsonElement data) {

        try {
            JsonObject trade = data.getAsJsonObject();
            Marker marker = Markers.appendRaw("data", trade.toString());
            log.info(marker, "Trade", kv("stream", stream));

        } catch (Exception e) {
            log.error("Error processing trade message: {}", e.getMessage(), e);
        }

    }
}
