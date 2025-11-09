package com.kubestock.inventory.dto;

import com.kubestock.inventory.model.StockStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductStockResponse {

    private Long id;
    private String sku;
    private String productName;
    private Integer quantity;
    private Integer reorderLevel;
    private Integer damagedQuantity;
    private BigDecimal unitPrice;
    private String location;
    private StockStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
