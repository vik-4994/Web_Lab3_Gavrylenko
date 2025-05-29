package org.example.lab3web.binance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BinanceStreamMessage {

    @JsonProperty("stream")
    private String stream;

    @JsonProperty("data")
    private BinanceTickerMessage data;
}
