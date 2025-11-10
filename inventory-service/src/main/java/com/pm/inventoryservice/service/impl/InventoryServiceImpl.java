package com.pm.inventoryservice.service.impl;

import com.pm.inventoryservice.dto.request.InventoryRequest;
import com.pm.inventoryservice.dto.response.InventoryResponse;
import com.pm.inventoryservice.mapper.InventoryMapper;
import com.pm.inventoryservice.model.Inventory;
import com.pm.inventoryservice.model.OrderStatus;
import com.pm.inventoryservice.repo.InventoryRepository;
import com.pm.inventoryservice.service.InventoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;

    public InventoryServiceImpl(
            InventoryRepository inventoryRepository,
            InventoryMapper inventoryMapper
    ) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryMapper = inventoryMapper;
    }

    @Override
    public InventoryResponse createInventory(InventoryRequest inventoryRequest) {

        Inventory newInventory = new Inventory();

        newInventory.setOrderId(inventoryRequest.getOrderId());
        newInventory.setProductId(inventoryRequest.getProductId());
        newInventory.setProductName(inventoryRequest.getProductName());
        newInventory.setOrderStatus(OrderStatus.ORDER_CREATED);

        inventoryRepository.save(newInventory);

        return inventoryMapper.toInventoryResponse(newInventory);

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
