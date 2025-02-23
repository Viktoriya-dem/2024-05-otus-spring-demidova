CREATE TABLE users
(
    id       uuid primary key,
    username VARCHAR(100),
    password VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS product
(
    id   uuid primary key,
    name VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS category
(
    id   uuid primary key,
    name VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS unit
(
    id   uuid primary key,
    name VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS attachment
(
    id   uuid primary key,
    name VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS recipe
(
    id      uuid primary key,
    name VARCHAR(100),
    description VARCHAR(100),
    cooking VARCHAR(1000),
    attachment_id uuid,
    user_id uuid,
    category_id uuid,
    created_date DATE,
    FOREIGN KEY (attachment_id) REFERENCES attachment(id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (category_id) REFERENCES category(id)
);

CREATE TABLE IF NOT EXISTS ingredient
(
    id         uuid primary key,
    unit_id    uuid,
    recipe_id  uuid,
    product_id uuid,
    quantity   float,
    FOREIGN KEY (unit_id) REFERENCES unit(id),
    FOREIGN KEY (recipe_id) REFERENCES recipe(id)
);