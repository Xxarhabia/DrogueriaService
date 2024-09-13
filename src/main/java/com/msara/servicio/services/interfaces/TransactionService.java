package com.msara.servicio.services.interfaces;

import com.msara.servicio.controllers.dto.response.TransactionSaleResponse;

public interface TransactionService {

    TransactionSaleResponse buyProduct(Long userId, boolean generateVoucher);
    void changeProduct();
    void returnProduct();
}
