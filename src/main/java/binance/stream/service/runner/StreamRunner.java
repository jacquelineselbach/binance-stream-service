package binance.stream.service.runner;

import binance.stream.service.client.WebSocketClient;
import binance.stream.service.config.BinanceConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class StreamRunner implements CommandLineRunner {

    private static final String BASE_URL = "wss://stream.binance.com:9443";
    private final BinanceConfig config;

    public StreamRunner(BinanceConfig config) {
        this.config = config;
    }

    @Override
    public void run(String... args) throws Exception {
        List<String> streams = collectStreams();

        if (streams.isEmpty()) {
            throw new IllegalStateException("No Binance streams configured in application.yml");
        }

        String socketUrl = buildSocketUrl(streams);
        log.info("Connecting to: {}", socketUrl);

        WebSocketClient client = new WebSocketClient(new URI(socketUrl));
        client.connectBlocking();

        log.info("Connected to Binance WebSocket stream.");
    }

    private List<String> collectStreams() {
        List<String> streams = new ArrayList<>();

        if (config.getSymbols() != null && config.getStreams() != null) {
            config.getSymbols().forEach(symbol ->
                    config.getStreams().forEach(stream ->
                            streams.add(symbol.toLowerCase() + stream))
            );
        }

        if (config.getGlobalStreams() != null && !config.getGlobalStreams().isEmpty()) {
            streams.addAll(config.getGlobalStreams());
        }

        return streams;
    }

    private String buildSocketUrl(List<String> streams) {

        if (streams.size() == 1 && streams.getFirst().startsWith("!")) {
            return BASE_URL + "/ws/" + streams.getFirst();
        }

        return BASE_URL + "/stream?streams=" + String.join("/", streams);

    }
}
