package com.mosscorp.stockservice.controller;

import com.mosscorp.stockservice.client.impl.AlphaVantageStockClient;
import com.mosscorp.stockservice.client.StockClient;
import com.mosscorp.stockservice.dao.impl.DynamoDBStockDao;
import com.mosscorp.stockservice.dao.StockDao;
import com.mosscorp.stockservice.model.CreateStockTransactionRequest;
import com.mosscorp.stockservice.model.StockTransaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class StockController {

    private final StockClient stockClient;
    private final StockDao stockDao;

    public StockController(final StockClient stockClient, final StockDao stockDao) {
        this.stockClient = stockClient;
        this.stockDao = stockDao;
    }

    @PostMapping("/stocks/{userId}/buy")
    public ResponseEntity<String> buyStocks(@PathVariable final String userId, @RequestBody final CreateStockTransactionRequest request) {
        final String purchaseId = UUID.randomUUID().toString();

        // fetch stock price
        final double stockPrice = stockClient.fetchStockPrice(request.symbol());
        final double quantity = request.value() / stockPrice;

        // store data in db
        stockDao.buyStocks(StockTransaction.fromRequest(request, purchaseId, userId, quantity));

        // return ID
        return ResponseEntity.ok(purchaseId);
    }

    @GetMapping("/stocks/{userId}")
    public ResponseEntity<List<StockTransaction>> listStockTransactions(@PathVariable final String userId) {
        final List<StockTransaction> stockTransactions = stockDao.listTransactions(userId);
        return ResponseEntity.ok(stockTransactions);
    }
}
