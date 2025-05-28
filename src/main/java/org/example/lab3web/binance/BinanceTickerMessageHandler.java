package org.example.lab3web.binance;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.lab3web.tickers.TickerUpdateListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BinanceTickerMessageHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TickerUpdateListener tickerUpdateListener;

    public void handleMessage(String payload) {
        try {
            BinanceTickerMessage tickerMessage = objectMapper.readValue(payload, BinanceTickerMessage.class);
            tickerUpdateListener.onTickerUpdate(tickerMessage);
        } catch (Exception e) {
            log.error("Failed to parse ticker message from Binance: {}", payload, e);
        }
    }
}
