INSERT INTO product_stock (sku, product_name, quantity, reorder_level, damaged_quantity, unit_price, location, status)
VALUES 
    ('SKU-001', 'Laptop Dell XPS 15', 50, 10, 0, 1299.99, 'Warehouse-A-Shelf-01', 'AVAILABLE'),
    ('SKU-002', 'Wireless Mouse Logitech', 200, 50, 5, 29.99, 'Warehouse-A-Shelf-05', 'AVAILABLE'),
    ('SKU-003', 'USB-C Cable 2M', 500, 100, 10, 12.99, 'Warehouse-B-Shelf-10', 'AVAILABLE'),
    ('SKU-004', 'Monitor Samsung 27"', 30, 15, 2, 349.99, 'Warehouse-A-Shelf-03', 'AVAILABLE'),
    ('SKU-005', 'Keyboard Mechanical RGB', 8, 20, 0, 89.99, 'Warehouse-A-Shelf-06', 'LOW_STOCK');

COMMENT ON TABLE product_stock IS 'Sample data inserted for testing purposes';
