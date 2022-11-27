package com.mosscorp.stockservice.dao;

import com.mosscorp.stockservice.model.StockTransaction;

import java.util.List;

public interface StockDao {
    void buyStocks(StockTransaction stockTransaction);
    List<StockTransaction> listTransactions(String userId);
}
