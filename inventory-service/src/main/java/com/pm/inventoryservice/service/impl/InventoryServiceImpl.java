package com.pm.inventoryservice.service.impl;

import com.pm.inventoryservice.dto.request.InventoryRequest;
import com.pm.inventoryservice.dto.response.InventoryResponse;
import com.pm.inventoryservice.exception.CustomBusinessException;
import com.pm.inventoryservice.exception.NotFoundException;
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
    public void createInventory(InventoryRequest inventoryRequest) {

        Inventory newInventory = new Inventory();

        newInventory.setOrderId(inventoryRequest.getOrderId());
        newInventory.setProductId(inventoryRequest.getProductId());
        newInventory.setProductName(inventoryRequest.getProductName());
        newInventory.setOrderStatus(OrderStatus.ORDER_CREATED);

        inventoryRepository.save(newInventory);
    }

    @Override
    public void updateInventory(InventoryRequest inventoryRequest, long orderId) {

        Inventory exisitinInventory = inventoryRepository.findByOrderId(orderId)
                .orElseThrow(()-> new NotFoundException("Inventory not found with order id " + orderId));

        exisitinInventory.setProductId(inventoryRequest.getProductId());
        exisitinInventory.setProductName(inventoryRequest.getProductName());
        exisitinInventory.setOrderStatus(OrderStatus.ORDER_UPDATED);

        inventoryRepository.save(exisitinInventory);
    }

    @Override
    public void deleteInventory(long inventoryId) {

        Inventory exisitingInventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(()-> new NotFoundException("Inventory not found with id " + inventoryId));

        if (exisitingInventory.getOrderStatus() != OrderStatus.ORDER_DELETED) {
            throw new CustomBusinessException("Order status not deleted");
        }

        inventoryRepository.delete(exisitingInventory);
    }

    @Override
    public InventoryResponse getInventory(long inventoryId) {
        Inventory exisitingInventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(()-> new NotFoundException("Inventory not found with id " + inventoryId));

        return inventoryMapper.toInventoryResponse(exisitingInventory);
    }

    @Override
    public List<InventoryResponse> getAllInventory() {
        List<Inventory> inventories = inventoryRepository.findAll();

        return inventories
                .stream()
                .map(inventoryMapper::toInventoryResponse)
                .toList();
    }

    @Override
    public void markAsDeletedOrder(long orderId) {

        Inventory exisitingInventory = inventoryRepository.findByOrderId(orderId)
                .orElseThrow(()-> new NotFoundException("Inventory not found with order id " + orderId));

        exisitingInventory.setOrderStatus(OrderStatus.ORDER_DELETED);
        inventoryRepository.save(exisitingInventory);
    }
}
