package com.kubestock.inventory.dto;

import com.kubestock.inventory.model.StockStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductStockRequest {

    @NotBlank(message = "SKU is required")
    @Size(max = 100, message = "SKU must not exceed 100 characters")
    private String sku;

    @NotBlank(message = "Product name is required")
    @Size(max = 200, message = "Product name must not exceed 200 characters")
    private String productName;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity must be non-negative")
    private Integer quantity;

    @NotNull(message = "Reorder level is required")
    @Min(value = 0, message = "Reorder level must be non-negative")
    private Integer reorderLevel;

    @DecimalMin(value = "0.00", message = "Unit price must be non-negative")
    @Digits(integer = 8, fraction = 2, message = "Invalid unit price format")
    private BigDecimal unitPrice;

    @Size(max = 100, message = "Location must not exceed 100 characters")
    private String location;

    private StockStatus status;
}
