insert into authors(full_name)
values ('Author_1'),
       ('Author_2'),
       ('Author_3');

insert into genres(name)
values ('Genre_1'),
       ('Genre_2'),
       ('Genre_3'),
       ('Genre_4'),
       ('Genre_5'),
       ('Genre_6');

insert into books(title, author_id)
values ('BookTitle_1', 1),
       ('BookTitle_2', 2),
       ('BookTitle_3', 3);

insert into books_genres(book_id, genre_id)
values (1, 1),
       (1, 2),
       (2, 3),
       (2, 4),
       (3, 5),
       (3, 6);

insert into users(username, password, authority)
values ('user', '$2a$12$MUy14RyQN94I1TnwlljvGuCgTya0V9BGFMehIX33YS4AOx7Anizuu', 'USER'),
       ('admin', '$2a$12$19e2flVSx38PD3v1lkn20.FUWhqSr/DJWn5mpPnwJsmxXQS.nSQCK', 'ADMIN');

INSERT INTO acl_sid (id, principal, sid)
VALUES (1, 1, 'user'),
       (2, 1, 'admin');

INSERT INTO acl_class (id, class)
VALUES (1, 'ru.otus.hw.dto.BookDto'),
       (2, 'ru.otus.hw.dto.BookDtoFullInfo'),
       (3, 'ru.otus.hw.models.Book');

INSERT INTO acl_object_identity
(id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
VALUES (1, 1, 1, NULL, 2, 0),
       (2, 1, 2, NULL, 2, 0),
       (3, 1, 3, NULL, 2, 0),
       (4, 2, 1, NULL, 2, 0),
       (5, 2, 2, NULL, 2, 0),
       (6, 2, 3, NULL, 2, 0),
       (7, 3, 1, NULL, 2, 0),
       (8, 3, 2, NULL, 2, 0),
       (9, 3, 3, NULL, 2, 0);

INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask,
                       granting, audit_success, audit_failure)
VALUES (1, 1, 1, 1, 1, 1, 1),
       (2, 1, 2, 1, 1, 1, 1),
       (3, 1, 2, 1, 1, 1, 1),
       (4, 1, 1, 1, 1, 1, 1),
       (5, 1, 2, 1, 1, 1, 1),
       (6, 1, 2, 1, 1, 1, 1),
       (2, 2, 2, 2, 1, 1, 1),
       (3, 2, 2, 2, 1, 1, 1),
       (5, 2, 2, 2, 1, 1, 1),
       (6, 2, 2, 2, 1, 1, 1),
       (7, 1, 2, 2, 1, 1, 1),
       (8, 1, 2, 2, 1, 1, 1),
       (9, 1, 2, 2, 1, 1, 1);

