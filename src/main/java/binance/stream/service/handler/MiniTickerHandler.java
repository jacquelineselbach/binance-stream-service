package binance.stream.service.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import java.time.Instant;
import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
public class MiniTickerHandler implements EventHandler {

    @Override
    public void handle(String stream, JsonElement data) {
        JsonObject obj = data.getAsJsonObject();

        log.info("MiniTicker event {} {} {} {} {} {} {} {} {}",
                kv("event", "MiniTicker"),
                kv("timestamp", Instant.now().toString()),
                kv("stream", stream),
                kv("symbol", obj.get("s").getAsString()),
                kv("lastPrice", obj.get("c").getAsString()),
                kv("openPrice", obj.get("o").getAsString()),
                kv("highPrice", obj.get("h").getAsString()),
                kv("lowPrice", obj.get("l").getAsString()),
                kv("volume", obj.get("v").getAsString()));
    }
}
