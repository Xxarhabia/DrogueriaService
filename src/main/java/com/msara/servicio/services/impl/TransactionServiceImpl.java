package com.msara.servicio.services.impl;

import com.msara.servicio.domain.repositories.ProductRepository;
import com.msara.servicio.domain.repositories.UserRepository;
import com.msara.servicio.services.interfaces.TransactionService;
import com.msara.servicio.services.exceptions.InsufficientStockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void buyProduct(Long id) {
        int countStock = productRepository.findStockByProductId(id);
        if(countStock == 0) {
            throw new InsufficientStockException("Te product stock is insufficient");
        }

    }

    @Override
    public void changeProduct() {

    }

    @Override
    public void returnProduct() {

    }
}
