package com.mosscorp.stockservice.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StockQueryResponse {
    @JsonAlias("Time Series (5min)")
    private Map<String, Map<String, String>> timeSeries;

    public Map<String, Map<String, String>> getTimeSeries() {
        return timeSeries;
    }
}
