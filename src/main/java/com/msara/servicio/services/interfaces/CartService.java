package com.msara.servicio.services.interfaces;

public interface CartService {

    void addProductToCart(Long userId, Long productId, int quantity);

    void removeProductFromCart();
}
