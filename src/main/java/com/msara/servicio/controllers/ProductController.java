package com.msara.servicio.controllers;

import com.msara.servicio.controllers.dto.request.ProductCreateRequest;
import com.msara.servicio.controllers.dto.response.ProductResponse;
import com.msara.servicio.domain.entities.ProductEntity;
import com.msara.servicio.services.impl.ProductServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductServiceImpl productService;

    @PostMapping("/create")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductCreateRequest productReq) {
        try {
            return new ResponseEntity<>(productService.createProduct(productReq), HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ProductResponse(
                    "An error occurred while creating the product", LocalDateTime.now(), false),
                    HttpStatus.CREATED);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findProduct(@PathVariable Long id) {
        try {
            return ResponseEntity.status(200).body(productService.findProduct(id));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @GetMapping()
    public ResponseEntity<?> findProducts() {
        try {
            return ResponseEntity.status(200).body(productService.findAllProducts());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody ProductEntity productReq) {
        try {
            return new ResponseEntity<ProductResponse>(productService.updateProduct(id, productReq), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<ProductResponse>(
                    new ProductResponse(
                            "A problem occurred updating the product", LocalDateTime.now(), false),
                            HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ProductResponse> deleteProduct(@PathVariable Long id) {
        try {
            return new ResponseEntity<ProductResponse>(productService.deleteProduct(id), HttpStatus.NO_CONTENT);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ProductResponse(
                    "Error occurred while deleting the product", LocalDateTime.now(), false),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
