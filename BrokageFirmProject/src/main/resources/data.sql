-- Insert mock data into asset table
INSERT INTO asset (id, customer_id, asset_id, asset_name, asset_desc, size, usable_size, created_at, updated_at, created_by, updated_by)
VALUES
    (NEXT VALUE FOR asset_seq, 'CUST003', 'AST001', 'Apple Inc.', 'Technology stock - AAPL', 200.0, 200.0, NOW(), NOW(), 'system', 'system'),
    (NEXT VALUE FOR asset_seq, 'CUST002', 'AST002', 'Tesla Inc.', 'Automotive & Energy stock - TSLA', 50.0, 45.0, NOW(), NOW(), 'system', 'system'),
    (NEXT VALUE FOR asset_seq, 'CUST004', 'AST003', 'Bitcoin', 'Cryptocurrency - BTC', 2.5, 2.0, NOW(), NOW(), 'system', 'system'),
    (NEXT VALUE FOR asset_seq, 'CUST003', 'AST004', 'Amazon.com Inc.', 'E-commerce stock - AMZN', 70.0, 65.0, NOW(), NOW(), 'system', 'system'),
    (NEXT VALUE FOR asset_seq, 'CUST001', 'AST005', 'TRY', 'Turkish Lira', 7000.0, 7000.0, NOW(), NOW(), 'system', 'system'),
    (NEXT VALUE FOR asset_seq, 'CUST003', 'AST005', 'TRY', 'Turkish Lira', 7000.0, 7000.0, NOW(), NOW(), 'system', 'system');

-- Insert mock data into order table
INSERT INTO "'order'" (id, order_id, customer_id, asset_id, asset_name, order_status, order_side, price, size, created_at, updated_at, created_by, updated_by)
VALUES
    (NEXT VALUE FOR order_seq, 'ORD001', 'CUST001', 1,'Apple Inc.', 'PENDING', 'BUY', 175.50, 10, '2025-08-14', NOW(), 'system', 'system'),
    (NEXT VALUE FOR order_seq, 'ORD002', 'CUST003', 1,'Apple Inc.', 'PENDING', 'SELL', 175.00, 5, '2025-08-19', NOW(), 'system', 'system'),
    (NEXT VALUE FOR order_seq, 'ORD005', 'CUST003', 1,'Apple Inc.', 'PENDING', 'SELL', 180.00, 3, '2025-08-19', NOW(), 'system', 'system'),
    (NEXT VALUE FOR order_seq, 'ORD003', 'CUST002', 2,'Tesla Inc.', 'PENDING', 'BUY', 720.75, 2, '2025-08-20', NOW(), 'system', 'system'),
    (NEXT VALUE FOR order_seq, 'ORD004', 'CUST004', 3,'Bitcoin', 'PENDING', 'BUY', 4300.00, 0.1, '2025-08-21', NOW(), 'system', 'system'),
    (NEXT VALUE FOR order_seq, 'ORD006', 'CUST003', 1, 'Apple Inc.', 'PENDING', 'SELL', 110.00, 3, NOW(), NOW(), 'system', 'system'),
    (NEXT VALUE FOR order_seq, 'ORD007', 'CUST006', 1, 'Apple Inc.', 'PENDING', 'SELL', 120.50, 5, NOW(), NOW(), 'system', 'system'),
    (NEXT VALUE FOR order_seq, 'ORD008', 'CUST003', 1, 'Apple Inc.', 'PENDING', 'SELL', 140.0, 3, NOW(), NOW(), 'system', 'system'),
    (NEXT VALUE FOR order_seq, 'ORD009', 'CUST007', 2, 'Tesla Inc.', 'PENDING', 'SELL', 720.50, 2, NOW(), NOW(), 'system', 'system');
