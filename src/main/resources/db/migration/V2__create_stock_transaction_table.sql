CREATE TABLE IF NOT EXISTS stock_transaction (
    id BIGSERIAL PRIMARY KEY,
    product_stock_id BIGINT NOT NULL,
    transaction_type VARCHAR(20) NOT NULL,
    quantity INTEGER NOT NULL,
    quantity_before INTEGER NOT NULL,
    quantity_after INTEGER NOT NULL,
    reason VARCHAR(500),
    reference VARCHAR(100),
    transaction_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_stock_transaction_product_stock FOREIGN KEY (product_stock_id) 
        REFERENCES product_stock(id) ON DELETE CASCADE,
    CONSTRAINT chk_transaction_type CHECK (transaction_type IN 
        ('STOCK_IN', 'STOCK_OUT', 'ADJUSTMENT', 'DAMAGE', 'RETURN', 'TRANSFER')),
    CONSTRAINT chk_quantity_positive CHECK (quantity > 0)
);

CREATE INDEX idx_stock_transaction_product_stock_id ON stock_transaction(product_stock_id);
CREATE INDEX idx_stock_transaction_type ON stock_transaction(transaction_type);
CREATE INDEX idx_stock_transaction_date ON stock_transaction(transaction_date DESC);

COMMENT ON TABLE stock_transaction IS 'Records all stock movement transactions';
COMMENT ON COLUMN stock_transaction.transaction_type IS 'Type of stock transaction';
COMMENT ON COLUMN stock_transaction.quantity_before IS 'Stock quantity before transaction';
COMMENT ON COLUMN stock_transaction.quantity_after IS 'Stock quantity after transaction';
