package com.kubestock.inventory.controller;

import com.kubestock.inventory.dto.AuditLogResponse;
import com.kubestock.inventory.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/audit")
@RequiredArgsConstructor
@Tag(name = "Audit Logs", description = "APIs for accessing audit logs and system activity tracking")
public class AuditController {

    private final InventoryService inventoryService;

    @GetMapping
    @Operation(summary = "Get all audit logs", description = "Retrieves all audit logs across the system")
    public ResponseEntity<List<AuditLogResponse>> getAllAuditLogs() {
        return ResponseEntity.ok(inventoryService.getAllAuditLogs());
    }
}
