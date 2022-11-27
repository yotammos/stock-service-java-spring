package com.mosscorp.stockservice.model;

public record CreateStockTransactionRequest(String symbol, double value) {}
