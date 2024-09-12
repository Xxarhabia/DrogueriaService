package com.msara.servicio.services.interfaces;

public interface TransactionService {

    void buyProduct(Long userId, boolean generateVoucher);
    void changeProduct();
    void returnProduct();
}
