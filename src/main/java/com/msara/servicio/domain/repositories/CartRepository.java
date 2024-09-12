package com.msara.servicio.domain.repositories;

import com.msara.servicio.domain.entities.CartEntity;
import com.msara.servicio.domain.entities.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {

    CartEntity findByUserId(Long userId);

    List<CartItemEntity> findItemsByUserId(Long id);
}
