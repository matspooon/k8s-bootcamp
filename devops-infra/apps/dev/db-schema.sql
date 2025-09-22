CREATE TABLE memo (
    id SERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    memo VARCHAR(1000) NOT NULL,
    create_date TIMESTAMP NOT NULL,
    update_date TIMESTAMP NULL
);