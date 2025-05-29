package org.example.lab3web.binance;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.lab3web.tickers.TickerUpdateListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class BinanceTickerMessageHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TickerUpdateListener tickerUpdateListener;

    private static final Map<String, String> latestPrices = new ConcurrentHashMap<>();

    public void handleMessage(String payload) {
        try {
            BinanceStreamMessage wrapper = objectMapper.readValue(payload, BinanceStreamMessage.class);
            BinanceTickerMessage tickerMessage = wrapper.getData();
            tickerUpdateListener.onTickerUpdate(tickerMessage);
        } catch (Exception e) {
            log.error("❌ Failed to parse ticker message from Binance: {}", payload, e);
        }
    }


    public static Map<String, String> getLatestPrices() {
        return latestPrices;
    }
}
