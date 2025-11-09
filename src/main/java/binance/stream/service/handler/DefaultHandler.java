package binance.stream.service.handler;

import com.google.gson.JsonElement;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.marker.Markers;
import org.slf4j.Marker;

@Slf4j
public class DefaultHandler implements EventHandler {

    @Override
    public void handle(String stream, JsonElement data) {

        try {
            Marker marker = Markers.appendRaw("data", data.toString());
            log.info(marker, stream);
        } catch (Exception e) {
            log.error("Error processing unknown message: {}", e.getMessage(), e);
        }

    }
}
