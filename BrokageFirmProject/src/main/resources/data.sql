-- Insert mock data into asset table
INSERT INTO asset (id, customer_id, asset_id, asset_name, asset_desc, size, usable_size, created_at, updated_at, created_by, updated_by)
VALUES
    (NEXT VALUE FOR asset_seq, 'CUST001', 'AST001', 'Apple Inc.', 'Technology stock - AAPL', 100.0, 80.0, NOW(), NOW(), 'system', 'system'),
    (NEXT VALUE FOR asset_seq, 'CUST002', 'AST002', 'Tesla Inc.', 'Automotive & Energy stock - TSLA', 50.0, 45.0, NOW(), NOW(), 'system', 'system'),
    (NEXT VALUE FOR asset_seq, 'CUST004', 'AST003', 'Bitcoin', 'Cryptocurrency - BTC', 2.5, 2.0, NOW(), NOW(), 'system', 'system'),
    (NEXT VALUE FOR asset_seq, 'CUST003', 'AST004', 'Amazon.com Inc.', 'E-commerce stock - AMZN', 70.0, 65.0, NOW(), NOW(), 'system', 'system'),
    (NEXT VALUE FOR asset_seq, 'CUST004', 'AST005', 'TRY', 'Turkish Lira', 700.0, 650.0, NOW(), NOW(), 'system', 'system');

-- Insert mock data into order table
INSERT INTO "'order'" (id, order_id, customer_id, asset_id, asset_name, order_status, order_side, price, size, created_at, updated_at, created_by, updated_by)
VALUES
    (NEXT VALUE FOR order_seq, 'ORD001', 'CUST001', 1,'Apple Inc.', 'PENDING', 'BUY', 175.50, 10, '2025-08-14', NOW(), 'system', 'system'),
    (NEXT VALUE FOR order_seq, 'ORD002', 'CUST001', 1,'Apple Inc.', 'PENDING', 'SELL', 180.00, 5, '2025-08-19', NOW(), 'system', 'system'),
    (NEXT VALUE FOR order_seq, 'ORD003', 'CUST002', 2,'Tesla Inc.', 'PENDING', 'BUY', 720.75, 2, '2025-08-20', NOW(), 'system', 'system'),
    (NEXT VALUE FOR order_seq, 'ORD004', 'CUST003', 3,'Bitcoin', 'CANCELLED', 'BUY', 4300.00, 0.1, '2025-08-21', NOW(), 'system', 'system');

--
-- INSERT INTO "'order'" (id, order_id, customer_id,  asset_name, order_status, order_side, price, size, created_at, updated_at, created_by, updated_by)
-- VALUES
--     (1, 'ORD001', 'CUST001', 'Apple Inc.', 'PENDING', 'BUY', 175.50, 10, NOW(), NOW(), 'system', 'system'),
--     (2, 'ORD002', 'CUST001', 'Apple Inc.', 'PENDING', 'SELL', 180.00, 5, NOW(), NOW(), 'system', 'system'),
--     (3, 'ORD003', 'CUST002', 'Tesla Inc.', 'PENDING', 'BUY', 720.75, 2, NOW(), NOW(), 'system', 'system'),
--     (4, 'ORD004', 'CUST003', 'Bitcoin', 'CANCELLED', 'BUY', 43000.00, 0.1, NOW(), NOW(), 'system', 'system');
