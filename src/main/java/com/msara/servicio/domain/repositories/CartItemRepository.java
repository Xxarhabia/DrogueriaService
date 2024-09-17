package com.msara.servicio.domain.repositories;

import com.msara.servicio.domain.entities.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {

    @Query("SELECT COUNT(ci) FROM CartItemEntity ci WHERE ci.cart.id = :cartId AND ci.product.id = :productId")
    int countProductsInCartItem(@Param("cartId") Long cartId, @Param("productId") Long productId);

    CartItemEntity findByCartId(Long cartId);

    @Query("SELECT ci FROM CartItemEntity ci WHERE ci.cart.id = :cartId AND ci.product.id = :productId")
    Optional<CartItemEntity> findCardItemByCartIdAndProductId(@Param("cartId") Long cartId, @Param("productId") Long productId);
}
