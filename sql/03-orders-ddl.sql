DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;

CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    total NUMERIC(38, 2) NOT NULL,
    created_at TIMESTAMP
);

CREATE TABLE order_items (
    id BIGSERIAL PRIMARY KEY,
    book_id BIGINT,
    book_title VARCHAR(255),
    quantity INTEGER,
    unit_price NUMERIC(38, 2),
    subtotal NUMERIC(38, 2),
    order_id BIGINT,
    CONSTRAINT fk_order_items_orders
        FOREIGN KEY (order_id)
        REFERENCES orders(id)
);