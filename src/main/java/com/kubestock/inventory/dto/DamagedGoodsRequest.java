package com.kubestock.inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DamagedGoodsRequest {

    @NotNull(message = "Damaged quantity is required")
    @Min(value = 1, message = "Damaged quantity must be at least 1")
    private Integer damagedQuantity;

    @Size(max = 500, message = "Reason must not exceed 500 characters")
    private String reason;

    @Size(max = 100, message = "Reference must not exceed 100 characters")
    private String reference;
}
