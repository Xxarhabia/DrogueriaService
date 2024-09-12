package com.msara.servicio.domain.repositories;

import com.msara.servicio.domain.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    @Query("SELECT p.stock FROM ProductEntity p WHERE p.id = :id")
    Integer findStockByProductId(@Param("id") Long id);
}
