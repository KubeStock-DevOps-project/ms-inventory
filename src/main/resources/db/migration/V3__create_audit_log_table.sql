CREATE TABLE IF NOT EXISTS audit_log (
    id BIGSERIAL PRIMARY KEY,
    entity_type VARCHAR(50) NOT NULL,
    entity_id BIGINT NOT NULL,
    action VARCHAR(20) NOT NULL,
    old_value TEXT,
    new_value TEXT,
    performed_by VARCHAR(100),
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(45)
);

CREATE INDEX idx_audit_log_entity ON audit_log(entity_type, entity_id);
CREATE INDEX idx_audit_log_action ON audit_log(action);
CREATE INDEX idx_audit_log_timestamp ON audit_log(timestamp DESC);
CREATE INDEX idx_audit_log_performed_by ON audit_log(performed_by);

COMMENT ON TABLE audit_log IS 'Audit trail for all entity changes';
COMMENT ON COLUMN audit_log.entity_type IS 'Type of entity being audited';
COMMENT ON COLUMN audit_log.entity_id IS 'ID of the entity being audited';
COMMENT ON COLUMN audit_log.action IS 'Action performed (CREATE, UPDATE, DELETE, etc.)';
COMMENT ON COLUMN audit_log.old_value IS 'JSON representation of old state';
COMMENT ON COLUMN audit_log.new_value IS 'JSON representation of new state';
