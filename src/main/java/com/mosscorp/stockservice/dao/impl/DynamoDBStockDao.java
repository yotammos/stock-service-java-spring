package com.mosscorp.stockservice.dao.impl;

import com.mosscorp.stockservice.Constants;
import com.mosscorp.stockservice.dao.StockDao;
import com.mosscorp.stockservice.model.StockTransaction;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DynamoDBStockDao implements StockDao {

    private final DynamoDbClient client;

    public DynamoDBStockDao() {
        this.client = DynamoDbClient
                .builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(() -> AwsBasicCredentials.create(Constants.ACCESS_TOKEN, Constants.SECRET_TOKEN))
                .build();
    }

    @Override
    public void buyStocks(final StockTransaction stockTransaction) {
        final Map<String, AttributeValue> item = stockTransaction.toDdbItem();
        final PutItemRequest request = PutItemRequest.builder().item(item).tableName(Constants.STOCK_TABLE).build();
        client.putItem(request);
    }

    @Override
    public List<StockTransaction> listTransactions(String userId) {
        return client.scan(
                ScanRequest.builder().tableName(Constants.STOCK_TABLE).attributesToGet().build()
        )
                .items()
                .stream().map(StockTransaction::fromDdbItem)
                .collect(Collectors.toList());
    }
}
