package com.mosscorp.stockservice.client;

public interface StockClient {
    double fetchStockPrice(String symbol);
}
