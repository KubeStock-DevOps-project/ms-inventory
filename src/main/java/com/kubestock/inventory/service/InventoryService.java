package com.kubestock.inventory.service;

import com.kubestock.inventory.dto.*;
import com.kubestock.inventory.exception.*;
import com.kubestock.inventory.model.*;
import com.kubestock.inventory.repository.*;
import com.kubestock.inventory.util.EntityMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final ProductStockRepository productStockRepository;
    private final StockTransactionRepository stockTransactionRepository;
    private final AuditLogRepository auditLogRepository;
    private final EntityMapper entityMapper;
    private final AuditService auditService;

    @Transactional
    public ProductStockResponse createProductStock(ProductStockRequest request) {
        if (productStockRepository.existsBySku(request.getSku())) {
            throw new DuplicateResourceException("Product with SKU " + request.getSku() + " already exists");
        }

        ProductStock productStock = entityMapper.toEntity(request);
        updateStockStatus(productStock);
        ProductStock saved = productStockRepository.save(productStock);

        auditService.logAction("ProductStock", saved.getId(), "CREATE", null, saved);
        log.info("Created product stock with SKU: {}", saved.getSku());

        return entityMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ProductStockResponse getProductStockById(Long id) {
        ProductStock productStock = findProductStockById(id);
        return entityMapper.toResponse(productStock);
    }

    @Transactional(readOnly = true)
    public ProductStockResponse getProductStockBySku(String sku) {
        ProductStock productStock = productStockRepository.findBySku(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Product with SKU " + sku + " not found"));
        return entityMapper.toResponse(productStock);
    }

    @Transactional(readOnly = true)
    public List<ProductStockResponse> getAllProductStocks() {
        return productStockRepository.findAll().stream()
                .map(entityMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductStockResponse> getLowStockProducts() {
        return productStockRepository.findLowStockProducts().stream()
                .map(entityMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductStockResponse> getProductsWithDamagedStock() {
        return productStockRepository.findProductsWithDamagedStock().stream()
                .map(entityMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductStockResponse updateProductStock(Long id, ProductStockRequest request) {
        ProductStock existingStock = findProductStockById(id);
        ProductStock oldStock = cloneProductStock(existingStock);

        if (!existingStock.getSku().equals(request.getSku()) &&
                productStockRepository.existsBySku(request.getSku())) {
            throw new DuplicateResourceException("Product with SKU " + request.getSku() + " already exists");
        }

        existingStock.setSku(request.getSku());
        existingStock.setProductName(request.getProductName());
        existingStock.setQuantity(request.getQuantity());
        existingStock.setReorderLevel(request.getReorderLevel());
        existingStock.setUnitPrice(request.getUnitPrice());
        existingStock.setLocation(request.getLocation());
        if (request.getStatus() != null) {
            existingStock.setStatus(request.getStatus());
        }

        updateStockStatus(existingStock);
        ProductStock updated = productStockRepository.save(existingStock);

        auditService.logAction("ProductStock", updated.getId(), "UPDATE", oldStock, updated);
        log.info("Updated product stock with ID: {}", id);

        return entityMapper.toResponse(updated);
    }

    @Transactional
    public StockTransactionResponse adjustStock(Long id, StockAdjustmentRequest request) {
        ProductStock productStock = findProductStockById(id);
        ProductStock oldStock = cloneProductStock(productStock);

        int quantityBefore = productStock.getQuantity();
        int quantityChange = calculateQuantityChange(request.getTransactionType(), request.getQuantity());

        if (isStockOutOperation(request.getTransactionType())) {
            if (productStock.getQuantity() < request.getQuantity()) {
                throw new InsufficientStockException(
                        "Insufficient stock. Available: " + productStock.getQuantity() +
                                ", Requested: " + request.getQuantity());
            }
        }

        productStock.setQuantity(productStock.getQuantity() + quantityChange);
        updateStockStatus(productStock);
        productStockRepository.save(productStock);

        StockTransaction transaction = createStockTransaction(
                productStock, request.getTransactionType(), request.getQuantity(),
                quantityBefore, productStock.getQuantity(), request.getReason(), request.getReference());

        StockTransaction savedTransaction = stockTransactionRepository.save(transaction);

        auditService.logAction("ProductStock", productStock.getId(), "STOCK_ADJUSTMENT", oldStock, productStock);
        log.info("Stock adjusted for product ID: {}, Type: {}, Quantity: {}",
                id, request.getTransactionType(), request.getQuantity());

        return entityMapper.toTransactionResponse(savedTransaction);
    }

    @Transactional
    public StockTransactionResponse recordDamagedGoods(Long id, DamagedGoodsRequest request) {
        ProductStock productStock = findProductStockById(id);
        ProductStock oldStock = cloneProductStock(productStock);

        if (productStock.getQuantity() < request.getDamagedQuantity()) {
            throw new InsufficientStockException(
                    "Cannot record damaged goods. Available stock: " + productStock.getQuantity() +
                            ", Damaged quantity: " + request.getDamagedQuantity());
        }

        int quantityBefore = productStock.getQuantity();
        productStock.setQuantity(productStock.getQuantity() - request.getDamagedQuantity());
        productStock.setDamagedQuantity(productStock.getDamagedQuantity() + request.getDamagedQuantity());
        updateStockStatus(productStock);
        productStockRepository.save(productStock);

        StockTransaction transaction = createStockTransaction(
                productStock, TransactionType.DAMAGE, request.getDamagedQuantity(),
                quantityBefore, productStock.getQuantity(), request.getReason(), request.getReference());

        StockTransaction savedTransaction = stockTransactionRepository.save(transaction);

        auditService.logAction("ProductStock", productStock.getId(), "DAMAGED_GOODS", oldStock, productStock);
        log.info("Recorded damaged goods for product ID: {}, Quantity: {}", id, request.getDamagedQuantity());

        return entityMapper.toTransactionResponse(savedTransaction);
    }

    @Transactional
    public void deleteProductStock(Long id) {
        ProductStock productStock = findProductStockById(id);
        auditService.logAction("ProductStock", id, "DELETE", productStock, null);
        productStockRepository.delete(productStock);
        log.info("Deleted product stock with ID: {}", id);
    }

    @Transactional(readOnly = true)
    public List<StockTransactionResponse> getTransactionsByProductId(Long id) {
        findProductStockById(id);
        return stockTransactionRepository.findRecentTransactionsByProductStockId(id).stream()
                .map(entityMapper::toTransactionResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AuditLogResponse> getAuditLogsByProductId(Long id) {
        findProductStockById(id);
        return auditLogRepository.findByEntityTypeAndEntityId("ProductStock", id).stream()
                .map(entityMapper::toAuditLogResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AuditLogResponse> getAllAuditLogs() {
        return auditLogRepository.findAll().stream()
                .map(entityMapper::toAuditLogResponse)
                .collect(Collectors.toList());
    }

    private ProductStock findProductStockById(Long id) {
        return productStockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product stock with ID " + id + " not found"));
    }

    private void updateStockStatus(ProductStock productStock) {
        if (productStock.getQuantity() == 0) {
            productStock.setStatus(StockStatus.OUT_OF_STOCK);
        } else if (productStock.getQuantity() <= productStock.getReorderLevel()) {
            productStock.setStatus(StockStatus.LOW_STOCK);
        } else if (productStock.getStatus() != StockStatus.DISCONTINUED) {
            productStock.setStatus(StockStatus.AVAILABLE);
        }
    }

    private int calculateQuantityChange(TransactionType type, int quantity) {
        return switch (type) {
            case STOCK_IN, RETURN -> quantity;
            case STOCK_OUT, DAMAGE, TRANSFER -> -quantity;
            case ADJUSTMENT -> quantity;
        };
    }

    private boolean isStockOutOperation(TransactionType type) {
        return type == TransactionType.STOCK_OUT ||
                type == TransactionType.DAMAGE ||
                type == TransactionType.TRANSFER;
    }

    private StockTransaction createStockTransaction(ProductStock productStock, TransactionType type,
                                                     int quantity, int quantityBefore, int quantityAfter,
                                                     String reason, String reference) {
        StockTransaction transaction = new StockTransaction();
        transaction.setProductStock(productStock);
        transaction.setTransactionType(type);
        transaction.setQuantity(quantity);
        transaction.setQuantityBefore(quantityBefore);
        transaction.setQuantityAfter(quantityAfter);
        transaction.setReason(reason);
        transaction.setReference(reference);
        return transaction;
    }

    private ProductStock cloneProductStock(ProductStock source) {
        ProductStock clone = new ProductStock();
        clone.setId(source.getId());
        clone.setSku(source.getSku());
        clone.setProductName(source.getProductName());
        clone.setQuantity(source.getQuantity());
        clone.setReorderLevel(source.getReorderLevel());
        clone.setDamagedQuantity(source.getDamagedQuantity());
        clone.setUnitPrice(source.getUnitPrice());
        clone.setLocation(source.getLocation());
        clone.setStatus(source.getStatus());
        return clone;
    }
}
