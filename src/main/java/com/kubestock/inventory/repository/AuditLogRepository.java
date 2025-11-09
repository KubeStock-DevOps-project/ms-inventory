package com.kubestock.inventory.repository;

import com.kubestock.inventory.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByEntityTypeAndEntityId(String entityType, Long entityId);

    List<AuditLog> findByAction(String action);

    List<AuditLog> findByTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<AuditLog> findByEntityTypeOrderByTimestampDesc(String entityType);
}
