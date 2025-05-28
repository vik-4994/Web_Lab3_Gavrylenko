package org.example.lab3web.binance;

import org.example.lab3web.tickers.Coin;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.util.stream.Stream;

public class BinanceWebSocketTickerStreamClient extends TextWebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("WebSocket connected.");
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        System.out.println("Received: " + message.getPayload());
        // Тут можна парсити повідомлення з Binance і передавати далі користувачу
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        System.err.println("WebSocket error: " + exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("WebSocket closed: " + status.getReason());
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
        return coin.getTag().toLowerCase() + "usdt@ticker";
    }
}
