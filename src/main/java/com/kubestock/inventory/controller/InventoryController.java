package com.kubestock.inventory.controller;

import com.kubestock.inventory.dto.*;
import com.kubestock.inventory.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventory Management", description = "APIs for managing product stock and inventory operations")
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    @Operation(summary = "Create new product stock", description = "Creates a new product stock entry in the inventory")
    public ResponseEntity<ProductStockResponse> createProductStock(@Valid @RequestBody ProductStockRequest request) {
        return new ResponseEntity<>(inventoryService.createProductStock(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product stock by ID", description = "Retrieves product stock details by ID")
    public ResponseEntity<ProductStockResponse> getProductStockById(@PathVariable Long id) {
        return ResponseEntity.ok(inventoryService.getProductStockById(id));
    }

    @GetMapping("/sku/{sku}")
    @Operation(summary = "Get product stock by SKU", description = "Retrieves product stock details by SKU")
    public ResponseEntity<ProductStockResponse> getProductStockBySku(@PathVariable String sku) {
        return ResponseEntity.ok(inventoryService.getProductStockBySku(sku));
    }

    @GetMapping
    @Operation(summary = "Get all product stocks", description = "Retrieves all product stocks in the inventory")
    public ResponseEntity<List<ProductStockResponse>> getAllProductStocks() {
        return ResponseEntity.ok(inventoryService.getAllProductStocks());
    }

    @GetMapping("/low-stock")
    @Operation(summary = "Get low stock products", description = "Retrieves products with stock quantity at or below reorder level")
    public ResponseEntity<List<ProductStockResponse>> getLowStockProducts() {
        return ResponseEntity.ok(inventoryService.getLowStockProducts());
    }

    @GetMapping("/damaged")
    @Operation(summary = "Get products with damaged stock", description = "Retrieves products that have damaged goods recorded")
    public ResponseEntity<List<ProductStockResponse>> getProductsWithDamagedStock() {
        return ResponseEntity.ok(inventoryService.getProductsWithDamagedStock());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update product stock", description = "Updates an existing product stock entry")
    public ResponseEntity<ProductStockResponse> updateProductStock(
            @PathVariable Long id,
            @Valid @RequestBody ProductStockRequest request) {
        return ResponseEntity.ok(inventoryService.updateProductStock(id, request));
    }

    @PostMapping("/{id}/adjust")
    @Operation(summary = "Adjust stock quantity", description = "Adjusts stock quantity (IN/OUT/ADJUSTMENT/TRANSFER/RETURN)")
    public ResponseEntity<StockTransactionResponse> adjustStock(
            @PathVariable Long id,
            @Valid @RequestBody StockAdjustmentRequest request) {
        return ResponseEntity.ok(inventoryService.adjustStock(id, request));
    }

    @PostMapping("/{id}/damage")
    @Operation(summary = "Record damaged goods", description = "Records damaged goods and updates stock quantities")
    public ResponseEntity<StockTransactionResponse> recordDamagedGoods(
            @PathVariable Long id,
            @Valid @RequestBody DamagedGoodsRequest request) {
        return ResponseEntity.ok(inventoryService.recordDamagedGoods(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product stock", description = "Deletes a product stock entry from the inventory")
    public ResponseEntity<Void> deleteProductStock(@PathVariable Long id) {
        inventoryService.deleteProductStock(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/transactions")
    @Operation(summary = "Get product transactions", description = "Retrieves all stock transactions for a specific product")
    public ResponseEntity<List<StockTransactionResponse>> getTransactionsByProductId(@PathVariable Long id) {
        return ResponseEntity.ok(inventoryService.getTransactionsByProductId(id));
    }

    @GetMapping("/{id}/audit-logs")
    @Operation(summary = "Get product audit logs", description = "Retrieves all audit logs for a specific product")
    public ResponseEntity<List<AuditLogResponse>> getAuditLogsByProductId(@PathVariable Long id) {
        return ResponseEntity.ok(inventoryService.getAuditLogsByProductId(id));
    }
}
