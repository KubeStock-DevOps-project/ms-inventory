package com.kubestock.inventory.dto;

import com.kubestock.inventory.model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockTransactionResponse {

    private Long id;
    private Long productStockId;
    private String productSku;
    private String productName;
    private TransactionType transactionType;
    private Integer quantity;
    private Integer quantityBefore;
    private Integer quantityAfter;
    private String reason;
    private String reference;
    private LocalDateTime transactionDate;
}
