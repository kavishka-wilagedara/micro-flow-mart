package com.pm.productservice.service.impl;

import com.pm.productservice.dto.request.ProductRequest;
import com.pm.productservice.dto.response.ProductResponse;
import com.pm.productservice.exception.InvalidDateException;
import com.pm.productservice.exception.InvalidInputException;
import com.pm.productservice.exception.NotFoundException;
import com.pm.productservice.mapper.ProductMapper;
import com.pm.productservice.model.Product;
import com.pm.productservice.model.ProductStatus;
import com.pm.productservice.repo.ProductRepository;
import com.pm.productservice.service.ProductService;
import com.pm.productservice.util.ApplicationConstants;
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
        // Set product status
        setProductStatus(newProduct);

        productRepository.save(newProduct);

        return productMapper.toProductResponse(newProduct);
    }

    @Override
    public ProductResponse updateProduct(Long productId, ProductRequest productRequest) {

        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(
                        ApplicationConstants.PRODUCT_NOT_FOUND_MESSAGE + productId));

        // Validate date
        validateProductRequest(productRequest);
        // Set product details
        productMapper.updateProductFromRequest(productRequest, existingProduct);
        // Set product status
        setProductStatus(existingProduct);

        productRepository.save(existingProduct);

        return productMapper.toProductResponse(existingProduct);
    }

    @Override
    public void deleteProduct(long productId) {

        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(
                        ApplicationConstants.PRODUCT_NOT_FOUND_MESSAGE + productId));

        productRepository.delete(existingProduct);
    }

    @Override
    public ProductResponse getProduct(long productId) {

        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(()-> new NotFoundException(
                        ApplicationConstants.PRODUCT_NOT_FOUND_MESSAGE + productId));

        return productMapper.toProductResponse(existingProduct);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream().map(productMapper::toProductResponse).toList();
    }

    @Override
    public ProductResponse reduceProductQuantity(Long productId, int quantity) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(
                        ApplicationConstants.PRODUCT_NOT_FOUND_MESSAGE + productId
                ));

        int availableProductQuantity = existingProduct.getQuantity();

        if(quantity != 0){

            if (availableProductQuantity >= quantity) {
                existingProduct.setQuantity(existingProduct.getQuantity() - quantity);
                setProductStatus(existingProduct);
                productRepository.save(existingProduct);
                return productMapper.toProductResponse(existingProduct);
            }
            else{
                throw new InvalidInputException("Available product quantity: " + availableProductQuantity);
            }

        }
        else{
            throw new InvalidInputException("Order quantity should be greater than zero");
        }

    }

    private void validateProductRequest(ProductRequest productRequest) {

        LocalDate today = LocalDate.now();

        // Check the expiry date is valid
        if (productRequest.getExpiryDate().isBefore(today)) {
            throw new InvalidDateException("Expiry date cannot be before current date");
        }
    }

    private void setProductStatus(Product product){

        int productQuantity = product.getQuantity();

        if(productQuantity == 0){
            product.setStatus(ProductStatus.OUT_OF_STOCK);
        }
        else if(productQuantity < 10){
            product.setStatus(ProductStatus.LOW_STOCK);
        }
        else{
            product.setStatus(ProductStatus.IN_STOCK);
        }
    }
}
