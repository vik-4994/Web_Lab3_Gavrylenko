//package org.example.lab3web.service;
//
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.example.lab3web.binance.BinanceWebSocketTickerStreamClient;
//import org.example.lab3web.tickers.Coin;
//import org.springframework.stereotype.Service;
//import org.springframework.web.socket.client.WebSocketClient;
//import org.springframework.web.socket.client.standard.StandardWebSocketClient;
//
//import java.net.URI;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//import org.springframework.web.socket.client.WebSocketClient;
//import org.springframework.web.socket.client.standard.StandardWebSocketClient;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class BinanceWebSocketClient {
//
//    private final BinanceWebSocketTickerStreamClient streamClient;
//
//    @PostConstruct
//    public void start() {
//        try {
//            String streams = Stream.of(Coin.values())
//                    .map(Coin::getTag)
//                    .collect(Collectors.joining("/"));
//
//            String url = "wss://stream.binance.com:9443/stream?streams=" + streams;
//
//            WebSocketClient client = new StandardWebSocketClient();
//            client.doHandshake(streamClient, new URI(url));
//
//            log.info("WebSocket connection to Binance started with streams: {}", streams);
//        } catch (Exception e) {
//            log.error("Failed to start Binance WebSocket client", e);
//        }
//    }
//}
