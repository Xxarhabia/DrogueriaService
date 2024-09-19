package com.msara.servicio.services.interfaces;

import com.msara.servicio.controllers.dto.request.TransactionAnnulmentRequest;
import com.msara.servicio.controllers.dto.response.TransactionResponse;

import java.sql.SQLException;

public interface TransactionService {

    TransactionResponse buyProduct(Long userId, boolean generateVoucher) throws SQLException;
    TransactionResponse returnProduct(Long userId, TransactionAnnulmentRequest transactionAnnulmentRequest) throws SQLException;
    void changeProduct();
}
