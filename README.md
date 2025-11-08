# Binance Stream Service

A Spring Boot app that connects to the Binance WebSocket API to stream real-time crypto market data (trades, tickers, klines, etc.) and logs them as structured JSON.

## Configuration

Edit `src/main/resources/application.yaml` to define symbols and streams:

```
binance:
  symbols:
    - btcusdt
    - ethusdt
    - bnbusdt
  streams:
    - "@kline_1m"
    - "@trade"
    - "@miniTicker"
    - "@ticker"
  globalStreams:
    - "!miniTicker@arr"
```

## Streams

| Stream            | Handler                   | Description                   |
| ----------------- | ------------------------- | ----------------------------- |
| `@trade`          | `TradeHandler`            | Trade events                  |
| `@kline_1m`       | `KlineHandler`            | 1-minute candlestick          |
| `@miniTicker`     | `MiniTickerHandler`       | Lightweight ticker per symbol |
| `@ticker`         | `TickerHandler`           | Full 24h ticker               |
| `!miniTicker@arr` | `GlobalMiniTickerHandler` | Global mini ticker            |
| (default)         | `DefaultHandler`          | Unknown streams               |


## Run

```bash
   mvn clean package
```

```bash
   mvn spring-boot:run
```
