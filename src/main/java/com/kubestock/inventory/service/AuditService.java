package com.kubestock.inventory.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kubestock.inventory.model.AuditLog;
import com.kubestock.inventory.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {

    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void logAction(String entityType, Long entityId, String action, Object oldValue, Object newValue) {
        try {
            AuditLog auditLog = new AuditLog();
            auditLog.setEntityType(entityType);
            auditLog.setEntityId(entityId);
            auditLog.setAction(action);
            auditLog.setOldValue(oldValue != null ? objectMapper.writeValueAsString(oldValue) : null);
            auditLog.setNewValue(newValue != null ? objectMapper.writeValueAsString(newValue) : null);
            auditLog.setPerformedBy("system");
            auditLog.setIpAddress("127.0.0.1");

            auditLogRepository.save(auditLog);
            log.info("Audit log created: {} {} for entity {} with ID {}", action, entityType, entityType, entityId);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize audit log data", e);
        }
    }
}
