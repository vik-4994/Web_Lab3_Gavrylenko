package org.example.lab3web.websocket;

import org.example.lab3web.tickers.TickerUpdateListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final TickerUpdateListener tickerUpdateListener;
    private final ObjectMapper objectMapper;

    @Autowired
    public WebSocketHandler(TickerUpdateListener tickerUpdateListener, ObjectMapper objectMapper) {
        this.tickerUpdateListener = tickerUpdateListener;
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        var prices = tickerUpdateListener.getAllPrices();
        String json = objectMapper.writeValueAsString(prices);
        session.sendMessage(new TextMessage(json));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        if (payload.equals("get")) {
            var prices = tickerUpdateListener.getAllPrices();
            String json = objectMapper.writeValueAsString(prices);
            session.sendMessage(new TextMessage(json));
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        System.err.println("WebSocket error: " + exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("WebSocket closed: " + status);
    }
}
