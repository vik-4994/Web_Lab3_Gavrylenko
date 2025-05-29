package org.example.lab3web.binance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BinanceTickerMessage {

    @JsonProperty("s")
    private String symbol;

    @JsonProperty("c")
    private String lastPrice;

    @JsonProperty("P")
    private String priceChangePercent;

    @JsonProperty("v")
    private String volume;

    public String getSymbol() {
        return symbol;
    }

    public String getLastPrice() {
        return lastPrice;
    }

    public String getPriceChangePercent() {
        return priceChangePercent;
    }

    public String getVolume() {
        return volume;
    }
}
