package com.pm.productservice.mapper;

import com.pm.productservice.dto.request.ProductRequest;
import com.pm.productservice.dto.response.ProductResponse;
import com.pm.productservice.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductResponse toProductResponse(Product product);

    Product toProduct(ProductRequest productRequest);

    @Mapping(target= "id", ignore = true)
    void updateProductFromRequest(ProductRequest productRequest, @MappingTarget Product product);

}
