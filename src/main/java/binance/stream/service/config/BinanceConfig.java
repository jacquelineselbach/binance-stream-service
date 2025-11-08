package binance.stream.service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "binance")
public class BinanceConfig {
    private String symbol;
    private String interval;
    private List<String> symbols;
    private List<String> streams;
    private List<String> globalStreams;
}
