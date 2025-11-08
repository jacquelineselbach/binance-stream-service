package binance.stream.service.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import java.time.Instant;
import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
public class TickerHandler implements EventHandler {

    @Override
    public void handle(String stream, JsonElement data) {
        JsonObject obj = data.getAsJsonObject();

        log.info("Ticker event {} {} {} {} {} {} {} {} {} {} {}",
                kv("event", "Ticker"),
                kv("timestamp", Instant.now().toString()),
                kv("stream", stream),
                kv("symbol", obj.get("s").getAsString()),
                kv("lastPrice", obj.get("c").getAsString()),
                kv("priceChange", obj.get("p").getAsString()),
                kv("priceChangePercent", obj.get("P").getAsString()),
                kv("highPrice", obj.get("h").getAsString()),
                kv("lowPrice", obj.get("l").getAsString()),
                kv("volume", obj.get("v").getAsString()),
                kv("quoteVolume", obj.get("q").getAsString()));
    }
}
