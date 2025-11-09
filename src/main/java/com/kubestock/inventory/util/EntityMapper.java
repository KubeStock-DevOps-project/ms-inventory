package com.kubestock.inventory.util;

import com.kubestock.inventory.dto.*;
import com.kubestock.inventory.model.*;
import org.springframework.stereotype.Component;

@Component
public class EntityMapper {

    public ProductStock toEntity(ProductStockRequest request) {
        ProductStock productStock = new ProductStock();
        productStock.setSku(request.getSku());
        productStock.setProductName(request.getProductName());
        productStock.setQuantity(request.getQuantity());
        productStock.setReorderLevel(request.getReorderLevel());
        productStock.setUnitPrice(request.getUnitPrice());
        productStock.setLocation(request.getLocation());
        productStock.setStatus(request.getStatus() != null ? request.getStatus() : StockStatus.AVAILABLE);
        productStock.setDamagedQuantity(0);
        return productStock;
    }

    public ProductStockResponse toResponse(ProductStock productStock) {
        return new ProductStockResponse(
                productStock.getId(),
                productStock.getSku(),
                productStock.getProductName(),
                productStock.getQuantity(),
                productStock.getReorderLevel(),
                productStock.getDamagedQuantity(),
                productStock.getUnitPrice(),
                productStock.getLocation(),
                productStock.getStatus(),
                productStock.getCreatedAt(),
                productStock.getUpdatedAt()
        );
    }

    public StockTransactionResponse toTransactionResponse(StockTransaction transaction) {
        return new StockTransactionResponse(
                transaction.getId(),
                transaction.getProductStock().getId(),
                transaction.getProductStock().getSku(),
                transaction.getProductStock().getProductName(),
                transaction.getTransactionType(),
                transaction.getQuantity(),
                transaction.getQuantityBefore(),
                transaction.getQuantityAfter(),
                transaction.getReason(),
                transaction.getReference(),
                transaction.getTransactionDate()
        );
    }

    public AuditLogResponse toAuditLogResponse(AuditLog auditLog) {
        return new AuditLogResponse(
                auditLog.getId(),
                auditLog.getEntityType(),
                auditLog.getEntityId(),
                auditLog.getAction(),
                auditLog.getOldValue(),
                auditLog.getNewValue(),
                auditLog.getPerformedBy(),
                auditLog.getTimestamp(),
                auditLog.getIpAddress()
        );
    }
}
