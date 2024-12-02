create table if not exists authors (
                                       id uuid,
                                       full_name varchar (255),
                                       primary key(id)
);

create table if not exists genres (
                                      id uuid,
                                      name varchar (255),
                                      primary key(id)
);

create table if not exists books (
                                     id uuid,
                                     title varchar (255),
                                     author_id uuid references authors (id) on delete cascade,
                                     primary key (id)
);

create table if not exists books_genres (
                                            book_id uuid references books (id) on delete cascade,
                                            genre_id uuid references genres (id) on delete cascade,
                                            primary key (book_id,genre_id)
);

create table if not exists comments (
                                        id uuid,
                                        text varchar (255),
                                        book_id uuid references books(id) on delete cascade,
                                        primary key (id)
);