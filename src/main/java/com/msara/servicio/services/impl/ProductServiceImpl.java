package com.msara.servicio.services.impl;

import com.msara.servicio.controllers.dto.request.ProductCreateRequest;
import com.msara.servicio.controllers.dto.response.ProductResponse;
import com.msara.servicio.domain.entities.ProductEntity;
import com.msara.servicio.domain.repositories.ProductRepository;
import com.msara.servicio.services.interfaces.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public ProductResponse createProduct(ProductCreateRequest productRequest) {
        ProductEntity product = ProductEntity.builder()
                .name(productRequest.name())
                .reference(productRequest.reference())
                .amount(productRequest.amount())
                .stock(productRequest.stock())
                .category(productRequest.category())
                .build();
        productRepository.save(product);
        return new ProductResponse("The product has been created successfully", LocalDateTime.now(), true);

    }

    @Override
    public ProductEntity findProduct(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    public List<ProductEntity> findAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductEntity product) {
        ProductEntity productFound = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        productFound.setName(product.getName());
        productFound.setReference(product.getReference());
        productFound.setAmount(product.getAmount());
        productFound.setStock(product.getStock());

        productRepository.save(productFound);
        return new ProductResponse("The product was successfully updated", LocalDateTime.now(), true);
    }

    @Override
    public ProductResponse deleteProduct(Long id) {
            ProductEntity product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
            productRepository.delete(product);
            return new ProductResponse("The product has been deleted successfully", LocalDateTime.now(), true);
    }
}
