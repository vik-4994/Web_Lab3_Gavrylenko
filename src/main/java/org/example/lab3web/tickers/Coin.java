package org.example.lab3web.tickers;

public enum Coin {
    BTC("btcusdt"),
    ETH("ethusdt"),
    BNB("bnbusdt"),
    DOGE("dogeusdt");

    private final String tag;

    Coin(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
}