package com.msara.servicio.controllers;

import com.msara.servicio.domain.entities.ProductEntity;
import com.msara.servicio.services.impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductServiceImpl productService;

    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestBody ProductEntity productReq) {
        try {
            ProductEntity product = productService.createProduct(productReq);
            return ResponseEntity.status(201).body(product);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findProduct(@PathVariable Long id) {
        try {
            ProductEntity productFound = productService.findProduct(id);
            return ResponseEntity.status(200).body(productFound);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @GetMapping()
    public ResponseEntity<?> findProducts() {
        try {
            List<ProductEntity> products = productService.findAllProducts();
            return ResponseEntity.status(200).body(products);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody ProductEntity productReq) {
        try {
            ProductEntity product = productService.updateProduct(id, productReq);
            return ResponseEntity.status(201).body(product);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            String productDeleted = productService.deleteProduct(id);
            return ResponseEntity.status(201).body(productDeleted);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }
}
