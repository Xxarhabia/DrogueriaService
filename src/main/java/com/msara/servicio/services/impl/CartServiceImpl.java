package com.msara.servicio.services.impl;

import com.msara.servicio.domain.entities.CartEntity;
import com.msara.servicio.domain.entities.CartItemEntity;
import com.msara.servicio.domain.entities.ProductEntity;
import com.msara.servicio.domain.repositories.CartRepository;
import com.msara.servicio.domain.repositories.ProductRepository;
import com.msara.servicio.services.interfaces.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public void addProductToCart(Long userId, Long productId, int quantity) {
        CartEntity cart = cartRepository.findByUserId(userId);
        ProductEntity product = productRepository.findById(productId)
                 .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItemEntity cartItemEntity = CartItemEntity.builder()
                .cart(cart)
                .product(product)
                .quantity(quantity)
                .build();

        cart.getItems().add(cartItemEntity);
        cartRepository.save(cart);
    }

    @Override
    public void removeProductFromCart() {
        //TODO terminar
    }

}
