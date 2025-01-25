create table if not exists authors
(
    id        bigserial,
    full_name varchar(255),
    primary key (id)
);

create table if not exists genres
(
    id   bigserial,
    name varchar(255),
    primary key (id)
);

create table if not exists books
(
    id        bigserial,
    title     varchar(255),
    author_id bigint references authors (id) on delete cascade,
    primary key (id)
);

create table if not exists books_genres
(
    book_id  bigint references books (id) on delete cascade,
    genre_id bigint references genres (id) on delete cascade,
    primary key (book_id, genre_id)
);

create table if not exists comments
(
    id      bigserial,
    text    varchar(255),
    book_id bigint references books (id) on delete cascade,
    primary key (id)
);

create table if not exists users
(
    id        bigserial,
    username  varchar(255),
    password  varchar(255),
    authority varchar(255)
);

CREATE TABLE IF NOT EXISTS acl_sid
(
    id        bigint  NOT NULL AUTO_INCREMENT,
    principal tinyint NOT NULL,
    sid       varchar NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE acl_sid
    ADD CONSTRAINT unique_uk_1 UNIQUE (sid, principal);

CREATE TABLE IF NOT EXISTS acl_class
(
    id    bigint NOT NULL AUTO_INCREMENT,
    class varchar,
    PRIMARY KEY (id)
);

ALTER TABLE acl_class
    ADD CONSTRAINT unique_uk_2 UNIQUE (class);

CREATE TABLE IF NOT EXISTS acl_entry
(
    id                  bigint NOT NULL AUTO_INCREMENT,
    acl_object_identity bigint,
    ace_order           int,
    sid                 bigint,
    mask                int,
    granting            tinyint,
    audit_success       tinyint,
    audit_failure       tinyint,
    PRIMARY KEY (id)
);

ALTER TABLE acl_entry
    ADD CONSTRAINT unique_uk_4 UNIQUE (acl_object_identity, ace_order);

CREATE TABLE IF NOT EXISTS acl_object_identity
(
    id                 bigint NOT NULL AUTO_INCREMENT,
    object_id_class    bigint,
    object_id_identity bigint,
    parent_object      bigint DEFAULT NULL,
    owner_sid          bigint DEFAULT NULL,
    entries_inheriting tinyint,
    PRIMARY KEY (id)
);

ALTER TABLE acl_object_identity
    ADD CONSTRAINT unique_uk_3 UNIQUE (object_id_class, object_id_identity);

ALTER TABLE acl_entry
    ADD FOREIGN KEY (acl_object_identity) REFERENCES acl_object_identity (id);

ALTER TABLE acl_entry
    ADD FOREIGN KEY (sid) REFERENCES acl_sid (id);

--
-- Constraints for table acl_object_identity
--
ALTER TABLE acl_object_identity
    ADD FOREIGN KEY (parent_object) REFERENCES acl_object_identity (id);

ALTER TABLE acl_object_identity
    ADD FOREIGN KEY (object_id_class) REFERENCES acl_class (id);

ALTER TABLE acl_object_identity
    ADD FOREIGN KEY (owner_sid) REFERENCES acl_sid (id);

