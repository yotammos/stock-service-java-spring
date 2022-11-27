package com.mosscorp.stockservice.model;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;

// DynamoDB item
public record StockTransaction(String id, String userId, String symbol, double value, double quantity) {
    public static final String TRANSACTION_ID = "Id";
    public static final String USER_ID = "UserId";
    public static final String SYMBOL = "Symbol";
    public static final String VALUE = "Value";
    public static final String QUANTITY = "Quantity";

    public Map<String, AttributeValue> toDdbItem() {
        return Map.of(
                TRANSACTION_ID, AttributeValue.builder().s(id).build(),
                USER_ID, AttributeValue.builder().s(userId).build(),
                SYMBOL, AttributeValue.builder().s(symbol).build(),
                VALUE, AttributeValue.builder().n(String.valueOf(value)).build(),
                QUANTITY, AttributeValue.builder().n(String.valueOf(quantity)).build()
        );
    }

    public static StockTransaction fromDdbItem(final Map<String, AttributeValue> ddbMap) {
        return new StockTransaction(
                ddbMap.get(TRANSACTION_ID).s(),
                ddbMap.get(USER_ID).s(),
                ddbMap.get(SYMBOL).s(),
                Double.parseDouble(ddbMap.get(VALUE).n()),
                Double.parseDouble(ddbMap.get(QUANTITY).n())
        );
    }

    public static StockTransaction fromRequest(
            final CreateStockTransactionRequest request,
            final String purchaseId,
            final String userId,
            final double quantity
    ) {
        return new StockTransaction(purchaseId, userId, request.symbol(), request.value(), quantity);
    }
}
