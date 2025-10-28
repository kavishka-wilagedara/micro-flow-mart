package com.pm.productservice.service.impl;

import com.pm.productservice.dto.request.ProductRequest;
import com.pm.productservice.dto.response.ProductResponse;
import com.pm.productservice.exception.InvalidDateException;
import com.pm.productservice.mapper.ProductMapper;
import com.pm.productservice.model.Product;
import com.pm.productservice.repo.ProductRepository;
import com.pm.productservice.service.ProductService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository,ProductMapper productMapper) {
        this.productMapper = productMapper;
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {

        // Validate date
        validateProductRequest(productRequest);
        // Set product details
        Product newProduct = productMapper.toProduct(productRequest);

        productRepository.save(newProduct);

        return productMapper.toProductResponse(newProduct);
    }

    @Override
    public ProductResponse updateProduct(ProductRequest productRequest) {
        return null;
    }

    @Override
    public void deleteProduct(String productId) {

    }

    @Override
    public ProductResponse getProduct(String productId) {
        return null;
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return List.of();
    }

    private void validateProductRequest(ProductRequest productRequest) {

        LocalDate today = LocalDate.now();

        // Check the expiry date is valid
        if (productRequest.getExpiryDate().isBefore(today)) {
            throw new InvalidDateException("Expiry date cannot be before current date");
        }
    }
}
