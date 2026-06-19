DROP TABLE IF EXISTS books;

CREATE TABLE books (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    publication_date DATE NOT NULL,
    category VARCHAR(255) NOT NULL,
    isbn VARCHAR(255) NOT NULL UNIQUE,
    rating DOUBLE PRECISION,
    visible BOOLEAN NOT NULL,
    stock INTEGER,
    price NUMERIC(38, 2),
    description VARCHAR(1000),
    image_url VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);