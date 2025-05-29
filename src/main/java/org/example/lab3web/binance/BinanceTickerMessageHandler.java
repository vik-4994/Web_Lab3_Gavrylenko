package org.example.lab3web.binance;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.lab3web.websocket.PriceSocketHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BinanceTickerMessageHandler extends TextWebSocketHandler {

    private final PriceSocketHandler priceSocketHandler;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public BinanceTickerMessageHandler(PriceSocketHandler priceSocketHandler) {
        this.priceSocketHandler = priceSocketHandler;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JsonNode root = objectMapper.readTree(message.getPayload());
        JsonNode dataNode = root.get("data");

        List<BinanceTickerMessage> tickers;
        if (dataNode.isArray()) {
            tickers = objectMapper.convertValue(dataNode, new TypeReference<>() {});
        } else {
            BinanceTickerMessage one = objectMapper.convertValue(dataNode, BinanceTickerMessage.class);
            tickers = List.of(one);
        }

        Map<String, Object> data = new HashMap<>();
        for (BinanceTickerMessage ticker : tickers) {
            data.put(ticker.getSymbol(), Map.of(
                    "currentPrice", ticker.getLastPrice(),
                    "priceChangePercent", ticker.getPriceChangePercent(),
                    "volume", ticker.getVolume()
            ));
        }

        String json = objectMapper.writeValueAsString(data);
        priceSocketHandler.sendToAll(json);
    }
}