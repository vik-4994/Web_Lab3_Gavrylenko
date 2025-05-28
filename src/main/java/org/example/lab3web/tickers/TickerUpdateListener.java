package org.example.lab3web.tickers;

import org.example.lab3web.binance.BinanceTickerMessage;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TickerUpdateListener {

    private final Map<String, BinanceTickerMessage> lastPrices = new ConcurrentHashMap<>();

    public void onTickerUpdate(BinanceTickerMessage message) {
        lastPrices.put(message.getSymbol(), message);
    }

    public BinanceTickerMessage getLastPrice(String symbol) {
        return lastPrices.get(symbol);
    }

    public Map<String, BinanceTickerMessage> getAllPrices() {
        return lastPrices;
    }
}
