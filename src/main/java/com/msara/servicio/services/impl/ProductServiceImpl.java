package com.msara.servicio.services.impl;

import com.msara.servicio.domain.entities.ProductEntity;
import com.msara.servicio.domain.repositories.ProductRepository;
import com.msara.servicio.services.interfaces.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public ProductEntity createProduct(ProductEntity product) {
        return productRepository.save(product);
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
    public ProductEntity updateProduct(Long id, ProductEntity product) {
        ProductEntity productFound = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        productFound.setName(product.getName());
        productFound.setReference(product.getReference());
        productFound.setAmount(product.getAmount());
        productFound.setStock(product.getStock());

        return productRepository.save(productFound);
    }

    @Override
    public String deleteProduct(Long id) {
        ProductEntity product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(product);
        return "Product has been deleted";
    }
}
