package com.pm.inventoryservice.service;

import com.pm.inventoryservice.dto.request.InventoryRequest;
import com.pm.inventoryservice.dto.response.InventoryResponse;

import java.util.List;

public interface InventoryService {

    InventoryResponse createInventory(InventoryRequest inventoryRequest);
    InventoryResponse updateInventory(InventoryRequest inventoryRequest, long inventoryId);
    void deleteInventory(long inventoryId);
    InventoryResponse getInventory(long inventoryId);
    List<InventoryResponse> getAllInventory();
}
