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
                Map.entry("depthUpdate", new DepthHandler()),
                Map.entry("!ticker@arr", new GlobalTickerHandler()),
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
            JsonElement parsed = JsonParser.parseString(message);

            if (parsed.isJsonArray()) {
                handleGlobalArray(parsed);
                return;
            }

            JsonObject root = parsed.getAsJsonObject();
            JsonElement dataElement = root.get("data");
            if (dataElement == null) {
                log.warn("Received message without data element at {}", Instant.now());
                return;
            }

            String stream = root.has("stream") ? root.get("stream").getAsString() : "unknown";

            if (dataElement.isJsonArray()) {
                handleNestedArray(stream, dataElement);
                return;
            }

            handleObjectMessage(stream, dataElement.getAsJsonObject());

        } catch (JsonSyntaxException e) {
            log.error("Invalid JSON message: {}", e.getMessage());
        } catch (Exception e) {
            log.error("⚠ Error processing message: {}", e.getMessage(), e);
        }
    }

    private void handleGlobalArray(JsonElement data) {
        String uri = getURI().toString();
        String key = uri.contains("!ticker@arr") ? "!ticker@arr"
                : uri.contains("!miniTicker@arr") ? "!miniTicker@arr"
                : "default";
        handlers.getOrDefault(key, handlers.get("default")).handle(key, data);
    }

    private void handleNestedArray(String stream, JsonElement dataElement) {
        String key = stream.contains("!ticker@arr") ? "!ticker@arr"
                : stream.contains("!miniTicker@arr") ? "!miniTicker@arr"
                : "default";
        handlers.getOrDefault(key, handlers.get("default")).handle(stream, dataElement);
    }

    private void handleObjectMessage(String stream, JsonObject data) {

        String event = data.has("e") ? data.get("e").getAsString() : "default";

        if ("default".equals(event) && stream.contains("depth")) {
            handlers.get("depthUpdate").handle(stream, data);
            return;
        }

        handlers.getOrDefault(event, handlers.get("default")).handle(stream, data);
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
