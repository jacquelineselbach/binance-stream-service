package binance.stream.service.handler;

import com.google.gson.JsonElement;
import lombok.extern.slf4j.Slf4j;
import java.time.Instant;
import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
public class DefaultHandler implements EventHandler {

    @Override
    public void handle(String stream, JsonElement data) {
        log.info("Unknown event {} {} {}",
                kv("event", "Unknown"),
                kv("timestamp", Instant.now().toString()),
                kv("stream", stream),
                kv("data", data.toString()));
    }
}
