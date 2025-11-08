package binance.stream.service.handler;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import java.time.Instant;
import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
public class GlobalMiniTickerHandler implements EventHandler {

    @Override
    public void handle(String stream, JsonElement data) {
        JsonArray arr = data.getAsJsonArray();

        log.info("Global MiniTicker event {} {} {}",
                kv("event", "GlobalMiniTicker"),
                kv("timestamp", Instant.now().toString()),
                kv("symbolCount", arr.size()));

        for (JsonElement e : arr) {
            JsonObject obj = e.getAsJsonObject();
            log.info("Global MiniTicker entry {} {} {} {} {}",
                    kv("symbol", obj.get("s").getAsString()),
                    kv("lastPrice", obj.get("c").getAsString()),
                    kv("openPrice", obj.get("o").getAsString()),
                    kv("highPrice", obj.get("h").getAsString()),
                    kv("lowPrice", obj.get("l").getAsString()));
        }
    }
}
