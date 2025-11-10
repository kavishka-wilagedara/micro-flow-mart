package com.pm.inventoryservice.service;

import com.pm.inventoryservice.dto.request.InventoryRequest;
import com.pm.inventoryservice.dto.response.InventoryResponse;

import java.util.List;

public interface InventoryService {

    void createInventory(InventoryRequest inventoryRequest);
    void updateInventory(InventoryRequest inventoryRequest, long orderId);
    void deleteInventory(long inventoryId);
    InventoryResponse getInventory(long inventoryId);
    List<InventoryResponse> getAllInventory();
    void markAsDeletedOrder(long orderId);
}
