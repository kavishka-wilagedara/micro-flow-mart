package com.pm.productservice.controller;

import com.pm.productservice.dto.request.ProductRequest;
import com.pm.productservice.dto.response.ProductResponse;
import com.pm.productservice.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

        ProductResponse productResponse = productService.createProduct(productRequest);

        return ResponseEntity.ok().body(productResponse);
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<ProductResponse> update(
            @Valid @PathVariable Long productId,
            @RequestBody ProductRequest productRequest){

        ProductResponse productResponse = productService.updateProduct(productId, productRequest);

        return ResponseEntity.ok().body(productResponse);
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<Void> delete(@PathVariable Long productId){

        productService.deleteProduct(productId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("get/{productId}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long productId){

        ProductResponse productResponse = productService.getProduct(productId);

        return ResponseEntity.ok().body(productResponse);
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<ProductResponse>> getAllProduct(){

        List<ProductResponse> allProducts = productService.getAllProducts();

        return ResponseEntity.ok().body(allProducts);
    }

}
