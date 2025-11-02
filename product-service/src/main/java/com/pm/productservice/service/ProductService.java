package com.pm.productservice.service;

import com.pm.productservice.dto.request.ProductRequest;
import com.pm.productservice.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(ProductRequest productRequest) ;
    ProductResponse updateProduct(Long productId, ProductRequest productRequest);
    void deleteProduct(long productId);
    ProductResponse getProduct(long productId);
    List<ProductResponse> getAllProducts();
    ProductResponse reduceProductQuantity(Long productId, int quantity);
    ProductResponse rollbackProductQuantity(Long productId, int quantity);
}
