DROP TABLE IF EXISTS transaction;
CREATE TABLE transaction (transaction_id IDENTITY PRIMARY KEY, transaction_amount DECIMAL(20, 2), transaction_payment_type VARCHAR(50), transaction_status VARCHAR(50));
DROP TABLE IF EXISTS order_line;
CREATE TABLE order_line (order_line_id IDENTITY PRIMARY KEY, order_line_transaction_id BIGINT, order_line_product_name VARCHAR(255), order_line_qty SMALLINT, order_line_price DECIMAL(20, 2));