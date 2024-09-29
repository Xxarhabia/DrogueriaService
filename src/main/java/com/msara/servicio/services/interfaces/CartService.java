package com.msara.servicio.services.interfaces;

import com.msara.servicio.controllers.dto.request.CartRequest;
import com.msara.servicio.controllers.dto.response.CartAddItemResponse;
import com.msara.servicio.domain.entities.CartItemEntity;

import java.util.List;

public interface CartService {

    CartAddItemResponse addProductToCart(Long userId, CartRequest cartRequest);

    void removeProductFromCart();

    List<CartItemEntity> showItemsInCart();
}
