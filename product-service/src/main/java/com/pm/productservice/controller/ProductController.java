package com.pm.productservice.controller;

import com.pm.productservice.dto.request.ProductRequest;
import com.pm.productservice.dto.response.ProductResponse;
import com.pm.productservice.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    public ResponseEntity<ProductResponse> create(
            @Valid @RequestBody ProductRequest productRequest){

        return ResponseEntity.ok(productService.createProduct(productRequest));
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<ProductResponse> update(
            @Valid @PathVariable Long productId,
            @RequestBody ProductRequest productRequest){

        return ResponseEntity.ok(productService.updateProduct(productId, productRequest));
    }
}
