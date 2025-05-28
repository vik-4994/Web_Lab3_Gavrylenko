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
    private String currentPrice;

    @JsonProperty("P")
    private String priceChangePercent;

    @JsonProperty("h")
    private String highPrice;

    @JsonProperty("l")
    private String lowPrice;

    @JsonProperty("v")
    private String volume;

    // Додатково можеш додати інші поля, які надсилає Binance
}
