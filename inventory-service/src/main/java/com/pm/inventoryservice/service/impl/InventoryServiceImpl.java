package com.pm.inventoryservice.service.impl;

import com.pm.inventoryservice.dto.request.InventoryRequest;
import com.pm.inventoryservice.dto.response.InventoryResponse;
import com.pm.inventoryservice.service.InventoryService;

import java.util.List;

public class InventoryServiceImpl implements InventoryService {
    @Override
    public InventoryResponse createInventory(InventoryRequest inventoryRequest) {
        return null;
    }

    @Override
    public InventoryResponse updateInventory(InventoryRequest inventoryRequest, long inventoryId) {
        return null;
    }

    @Override
    public void deleteInventory(long inventoryId) {

    }

    @Override
    public InventoryResponse getInventory(long inventoryId) {
        return null;
    }

    @Override
    public List<InventoryResponse> getAllInventory() {
        return List.of();
    }
}
