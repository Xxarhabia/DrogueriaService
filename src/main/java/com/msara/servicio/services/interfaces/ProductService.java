package com.msara.servicio.services.interfaces;

import com.msara.servicio.domain.entities.ProductEntity;
import org.springframework.stereotype.Component;

import java.util.List;

public interface ProductService {

    ProductEntity createProduct(ProductEntity product);

    ProductEntity findProduct(Long id);

    List<ProductEntity> findAllProducts();

    ProductEntity updateProduct(Long id, ProductEntity product);

    String deleteProduct(Long id);
}
