package org.example.lab3web.controller;

import lombok.RequiredArgsConstructor;
import org.example.lab3web.binance.BinanceTickerMessage;
import org.example.lab3web.tickers.Coin;
import org.example.lab3web.tickers.TickerUpdateListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/crypto")
@RequiredArgsConstructor
public class CryptoController {

    private final TickerUpdateListener listener;

    @GetMapping
    public Map<String, BinanceTickerMessage> getAllPrices() {
        return listener.getAllPrices().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey(),
                        Map.Entry::getValue
                ));
    }
}
