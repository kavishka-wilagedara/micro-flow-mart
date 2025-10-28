package com.pm.productservice.mapper;

import com.pm.productservice.dto.request.ProductRequest;
import com.pm.productservice.dto.response.ProductResponse;
import com.pm.productservice.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductResponse toProductResponse(Product product);

    Product toProduct(ProductRequest productRequest);

}
