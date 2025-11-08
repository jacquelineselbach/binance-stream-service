package binance.stream.service.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import java.time.Instant;
import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
public class TradeHandler implements EventHandler {

    @Override
    public void handle(String stream, JsonElement data) {
        JsonObject obj = data.getAsJsonObject();

        log.info("Trade event {} {} {} {} {} {} {}",
                kv("event", "Trade"),
                kv("timestamp", Instant.now().toString()),
                kv("stream", stream),
                kv("symbol", obj.get("s").getAsString()),
                kv("price", obj.get("p").getAsString()),
                kv("quantity", obj.get("q").getAsString()),
                kv("buyerMaker", String.valueOf(obj.get("m").getAsBoolean())));
    }
}
