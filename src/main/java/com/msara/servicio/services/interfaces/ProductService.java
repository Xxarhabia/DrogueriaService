package com.msara.servicio.services.interfaces;

import com.msara.servicio.controllers.dto.request.ProductCreateRequest;
import com.msara.servicio.controllers.dto.response.ProductResponse;
import com.msara.servicio.domain.entities.ProductEntity;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(ProductCreateRequest product);

    ProductEntity findProduct(Long id);

    List<ProductEntity> findAllProducts();

    ProductResponse updateProduct(Long id, ProductEntity product);

    ProductResponse deleteProduct(Long id);
}
