package binance.stream.service.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import java.time.Instant;
import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
public class KlineHandler implements EventHandler {

    @Override
    public void handle(String stream, JsonElement data) {
        JsonObject k = data.getAsJsonObject().getAsJsonObject("k");

        log.info("Kline event {} {} {} {} {} {} {} {} {}",
                kv("event", "Kline"),
                kv("timestamp", Instant.now().toString()),
                kv("stream", stream),
                kv("symbol", k.get("s").getAsString()),
                kv("interval", k.get("i").getAsString()),
                kv("open", k.get("o").getAsString()),
                kv("close", k.get("c").getAsString()),
                kv("high", k.get("h").getAsString()),
                kv("low", k.get("l").getAsString()));
    }
}
