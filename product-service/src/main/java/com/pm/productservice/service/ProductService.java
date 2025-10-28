package com.pm.productservice.service;

import com.pm.productservice.dto.request.ProductRequest;
import com.pm.productservice.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(ProductRequest productRequest) ;
    ProductResponse updateProduct(ProductRequest productRequest);
    void deleteProduct(String productId);
    ProductResponse getProduct(String productId);
    List<ProductResponse> getAllProducts();
}
