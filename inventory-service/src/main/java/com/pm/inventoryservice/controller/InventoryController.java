package com.pm.inventoryservice.controller;

import com.pm.inventoryservice.dto.response.InventoryResponse;
import com.pm.inventoryservice.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @DeleteMapping("/delete/{inventoryId}")
    public ResponseEntity<String> deleteInventory(@PathVariable("inventoryId") long inventoryId) {

        inventoryService.deleteInventory(inventoryId);

        return ResponseEntity.ok().body("Deleted inventory");
    }

    @GetMapping("/get/{inventoryId}")
    public ResponseEntity<InventoryResponse> getInventory(@PathVariable("inventoryId") long inventoryId) {

        InventoryResponse inventoryResponse = inventoryService.getInventory(inventoryId);

        return ResponseEntity.ok().body(inventoryResponse);
    }
}
