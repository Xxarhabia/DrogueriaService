package com.msara.servicio.services.impl;

import com.msara.servicio.controllers.dto.request.CartRequest;
import com.msara.servicio.controllers.dto.response.CartAddItemResponse;
import com.msara.servicio.domain.entities.CartEntity;
import com.msara.servicio.domain.entities.CartItemEntity;
import com.msara.servicio.domain.entities.ProductEntity;
import com.msara.servicio.domain.repositories.CartItemRepository;
import com.msara.servicio.domain.repositories.CartRepository;
import com.msara.servicio.domain.repositories.ProductRepository;
import com.msara.servicio.services.exceptions.InsufficientStockException;
import com.msara.servicio.services.interfaces.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public CartAddItemResponse addProductToCart(Long userId, CartRequest cartRequest) {
        CartEntity cart = cartRepository.findByUserId(userId);
        int countStock = productRepository.findStockByProductId(cartRequest.productId());
        if(countStock == 0) {
            throw new InsufficientStockException("Te product stock is insufficient");
        }
        ProductEntity product = productRepository.findById(cartRequest.productId())
                 .orElseThrow(() -> new RuntimeException("Product not found"));

        if (cartItemRepository.countProductsInCartItem(cart.getId(), product.getId()) > 0) {
            CartItemEntity cartItemEntity = cartItemRepository.findCardItemByCartIdAndProductId(cart.getId(), product.getId()).orElseThrow();
            cartItemEntity.setQuantity(cartRequest.quantity() + cartItemEntity.getQuantity());

            cartItemRepository.save(cartItemEntity);

            return new CartAddItemResponse(
                    "The product was added successfully",
                    product.getName(),
                    cartRequest.quantity());
        } else {
            CartItemEntity cartItemEntity = CartItemEntity.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(cartRequest.quantity())
                    .build();

            cart.getItems().add(cartItemEntity);
            cartRepository.save(cart);

            return new CartAddItemResponse(
                    "The product was added successfully",
                    product.getName(),
                    cartRequest.quantity());
        }
    }

    @Override
    public void removeProductFromCart() {

    }

    @Override
    public List<CartItemEntity> showItemsInCart() {

        return null;
    }

}
