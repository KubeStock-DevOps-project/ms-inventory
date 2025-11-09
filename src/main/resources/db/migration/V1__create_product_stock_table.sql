CREATE TABLE IF NOT EXISTS product_stock (
    id BIGSERIAL PRIMARY KEY,
    sku VARCHAR(100) NOT NULL UNIQUE,
    product_name VARCHAR(200) NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 0,
    reorder_level INTEGER NOT NULL DEFAULT 0,
    damaged_quantity INTEGER NOT NULL DEFAULT 0,
    unit_price NUMERIC(10, 2),
    location VARCHAR(100),
    status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0,
    CONSTRAINT chk_quantity CHECK (quantity >= 0),
    CONSTRAINT chk_reorder_level CHECK (reorder_level >= 0),
    CONSTRAINT chk_damaged_quantity CHECK (damaged_quantity >= 0),
    CONSTRAINT chk_status CHECK (status IN ('AVAILABLE', 'LOW_STOCK', 'OUT_OF_STOCK', 'DISCONTINUED'))
);

CREATE INDEX idx_product_stock_sku ON product_stock(sku);
CREATE INDEX idx_product_stock_status ON product_stock(status);
CREATE INDEX idx_product_stock_quantity ON product_stock(quantity);

COMMENT ON TABLE product_stock IS 'Stores product stock information and quantities';
COMMENT ON COLUMN product_stock.sku IS 'Unique stock keeping unit identifier';
COMMENT ON COLUMN product_stock.damaged_quantity IS 'Quantity of damaged goods';
COMMENT ON COLUMN product_stock.version IS 'Optimistic locking version';
