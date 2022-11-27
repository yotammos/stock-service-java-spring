package com.mosscorp.stockservice.client.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mosscorp.stockservice.Constants;
import com.mosscorp.stockservice.client.StockClient;
import com.mosscorp.stockservice.model.StockQueryResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

@Component
public class AlphaVantageStockClient implements StockClient {

    private final static String CLOSE_MEMBER = "4. close";
    private final static String URL_FORMAT =
            "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=%s&interval=5min&apikey=" + Constants.ALPHA_VANTAGE_API_KEY;

    private final HttpClient client;
    private final ObjectMapper mapper;

    public AlphaVantageStockClient() {
        this.client = HttpClient.newBuilder().build();
        this.mapper = new ObjectMapper();
    }

    @Override
    public double fetchStockPrice(String symbol) {
        final String url = String.format(URL_FORMAT, symbol);
        try {
            final HttpResponse<String> response = client.send(
                    HttpRequest.newBuilder().GET().uri(URI.create(url)).build(),
                    HttpResponse.BodyHandlers.ofString()
            );
            return parseLatestStockValue(response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private double parseLatestStockValue(final String rawBody) throws JsonProcessingException {
        final StockQueryResponse response = mapper.readValue(rawBody, StockQueryResponse.class);
        final Map<String, String> latestData = response
                .getTimeSeries()
                .entrySet()
                .stream()
                .findFirst()
                .get()
                .getValue();
        return Double.parseDouble(latestData.get(CLOSE_MEMBER));
    }
}
