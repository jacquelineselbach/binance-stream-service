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

    private final BinanceConfig config;

    public StreamRunner(BinanceConfig config) {
        this.config = config;
    }

    @Override
    public void run(String... args) throws Exception {
        List<String> allStreams = new ArrayList<>();

        if (config.getSymbols() != null && config.getStreams() != null) {
            for (String symbol : config.getSymbols()) {
                for (String stream : config.getStreams()) {
                    allStreams.add(symbol.toLowerCase() + stream);
                }
            }
        }

        if (config.getGlobalStreams() != null && !config.getGlobalStreams().isEmpty()) {
            allStreams.addAll(config.getGlobalStreams());
        }

        if (allStreams.isEmpty()) {
            throw new IllegalStateException("No Binance streams configured in application.yml");
        }

        String socketUrl = "wss://stream.binance.com:9443/stream?streams=" + String.join("/", allStreams);
        log.info("Connecting to: {}", socketUrl);

        WebSocketClient client = new WebSocketClient(new URI(socketUrl));
        client.connectBlocking();

        log.info("Connected to Binance WebSocket stream.");
    }
}
