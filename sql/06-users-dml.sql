CREATE EXTENSION IF NOT EXISTS pgcrypto;

INSERT INTO app_users (id, email, password_hash, full_name, enabled, created_at, updated_at)
VALUES
(1, 'cliente@relatos.com', crypt('123456', gen_salt('bf', 10)), 'Cliente Demo', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO app_user_roles (user_id, role)
VALUES
(1, 'CUSTOMER');

SELECT setval('app_users_id_seq', (SELECT MAX(id) FROM app_users));
