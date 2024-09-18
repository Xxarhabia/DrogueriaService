package com.msara.servicio.services.interfaces;

import com.msara.servicio.controllers.dto.response.TransactionSaleResponse;

import java.sql.SQLException;

public interface TransactionService {

    TransactionSaleResponse buyProduct(Long userId, boolean generateVoucher) throws SQLException;
    void changeProduct();
    void returnProduct();
}
