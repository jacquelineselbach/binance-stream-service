package binance.stream.service.client;

import binance.stream.service.handler.*;
import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.time.Instant;
import java.util.Map;

@Slf4j
public class WebSocketClient extends org.java_websocket.client.WebSocketClient {

    private final Gson gson = new GsonBuilder().create();
    private final Map<String, EventHandler> handlers;

    public WebSocketClient(URI serverUri) {
        super(serverUri);
        this.handlers = Map.ofEntries(
                Map.entry("trade", new TradeHandler()),
                Map.entry("kline", new KlineHandler()),
                Map.entry("24hrTicker", new TickerHandler()),
                Map.entry("24hrMiniTicker", new MiniTickerHandler()),
                Map.entry("!miniTicker@arr", new GlobalMiniTickerHandler()),
                Map.entry("default", new DefaultHandler())
        );
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        log.info("Connected to Binance WebSocket stream at {}", getURI());
    }

    @Override
    public void onMessage(String message) {
        try {
            JsonObject json = gson.fromJson(message, JsonObject.class);
            String stream = json.has("stream") ? json.get("stream").getAsString() : "unknown";
            JsonElement dataElement = json.get("data");

            if (dataElement == null) {
                log.warn("Received message without data element at {}", Instant.now());
                return;
            }

            if (dataElement.isJsonArray()) {
                handlers.get("!miniTicker@arr").handle(stream, dataElement);
                return;
            }

            JsonObject data = dataElement.getAsJsonObject();
            String event = data.has("e") ? data.get("e").getAsString() : "default";

            EventHandler handler = handlers.getOrDefault(event, handlers.get("default"));
            handler.handle(stream, data);

        } catch (JsonSyntaxException e) {
            log.error("Invalid JSON message: {}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("⚠ Error processing message: {}", e.getMessage(), e);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        log.warn("Connection closed (code={}, remote={}): {}", code, remote, reason);
    }

    @Override
    public void onError(Exception ex) {
        log.error("WebSocket error: {}", ex.getMessage(), ex);
    }
}
