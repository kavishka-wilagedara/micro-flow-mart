package com.pm.inventoryservice.mapper;

import com.pm.inventoryservice.dto.response.InventoryResponse;
import com.pm.inventoryservice.model.Inventory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    InventoryResponse toInventoryResponse(Inventory inventory);

}
