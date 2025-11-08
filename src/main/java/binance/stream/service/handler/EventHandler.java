package binance.stream.service.handler;

import com.google.gson.JsonElement;

public interface EventHandler {
    void handle(String stream, JsonElement data);
}
