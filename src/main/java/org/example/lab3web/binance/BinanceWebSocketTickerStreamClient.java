package org.example.lab3web.binance;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.lab3web.tickers.Coin;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class BinanceWebSocketTickerStreamClient extends TextWebSocketHandler {

    private final BinanceTickerMessageHandler messageHandler;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("WebSocket connected to Binance.");
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        log.debug("Received: {}", message.getPayload());
        messageHandler.handleMessage(message.getPayload());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.error("WebSocket error: {}", exception.getMessage(), exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.warn("WebSocket closed: {}", status.getReason());
    }

    public boolean supportsPartialMessages() {
        return false;
    }

    public static String createStreamUrl() {
        String baseUrl = "wss://stream.binance.com:9443/stream?streams=";
        String streams = Stream.of(Coin.values())
                .map(BinanceWebSocketTickerStreamClient::createTicker)
                .reduce((first, second) -> first + "/" + second)
                .orElse("");
        return baseUrl + streams;
    }

    private static String createTicker(Coin coin) {
        return coin.getTag().toLowerCase() + "@ticker";
    }
}
