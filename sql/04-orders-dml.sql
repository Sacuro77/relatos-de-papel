INSERT INTO orders (id, user_id, status, total, created_at)
VALUES
(1, 'user-001', 'CREATED', 25.98, CURRENT_TIMESTAMP),
(2, 'user-002', 'CREATED', 12.99, CURRENT_TIMESTAMP),
(3, 'user-003', 'CREATED', 12.99, CURRENT_TIMESTAMP);

INSERT INTO order_items (id, book_id, book_title, quantity, unit_price, subtotal, order_id)
VALUES
(1, 2, 'El principito', 2, 12.99, 25.98, 1),
(2, 2, 'El principito', 1, 12.99, 12.99, 2),
(3, 2, 'El principito', 1, 12.99, 12.99, 3);

SELECT setval('orders_id_seq', (SELECT MAX(id) FROM orders));
SELECT setval('order_items_id_seq', (SELECT MAX(id) FROM order_items));