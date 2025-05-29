package org.example.lab3web.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.lab3web.binance.BinanceWebSocketTickerStreamClient;
import org.example.lab3web.tickers.Coin;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.net.URI;

@Slf4j
@Service
@RequiredArgsConstructor
public class BinanceWebSocketClient {

    private final BinanceWebSocketTickerStreamClient streamClient;

    @PostConstruct
    public void start() {
        try {
            WebSocketClient client = new StandardWebSocketClient();
            String uri = BinanceWebSocketTickerStreamClient.createStreamUrl();
            client.doHandshake(streamClient, uri);
            log.info("WebSocket connection to Binance started.");
        } catch (Exception e) {
            log.error("Failed to start Binance WebSocket client", e);
        }
    }

}
