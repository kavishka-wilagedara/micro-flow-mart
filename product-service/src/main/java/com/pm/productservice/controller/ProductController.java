package com.pm.productservice.controller;

import com.pm.productservice.dto.request.ProductRequest;
import com.pm.productservice.dto.response.ProductResponse;
import com.pm.productservice.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest productRequest){
        return ResponseEntity.ok(productService.createProduct(productRequest));
    }
}
