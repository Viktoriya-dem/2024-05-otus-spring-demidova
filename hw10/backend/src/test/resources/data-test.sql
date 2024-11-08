insert into authors(id, full_name)
values ('5f7019b2-382f-41fa-a8af-b46dc3e05252', 'Author_1'),
       ('30df0652-0b5d-40af-86d9-cd336b836648', 'Author_2'),
       ('57daec39-1f36-4248-abaf-9e12bb9e77f5', 'Author_3');

insert into genres(id, name)
values ('9fccd731-27a2-4639-b1f6-648087ef744b', 'Genre_1'),
       ('980fab3b-338d-45e7-83b6-29b98d1c4b02', 'Genre_2'),
       ('66f51d2d-e6b7-4602-8b21-31439ea1f721', 'Genre_3'),
       ('dc511c5a-1436-4b96-b727-3c88b1e423d4', 'Genre_4'),
       ('a15f63d1-8cb3-477a-a5ab-b1fc0ea00fd4', 'Genre_5'),
       ('03ce09ef-f30a-4459-867f-1705cce28da6', 'Genre_6');

insert into books(id, title, author_id)
values ('8b0f427f-1365-4883-8834-c6b25515b848', 'BookTitle_1', '5f7019b2-382f-41fa-a8af-b46dc3e05252'),
       ('f7b16ec4-3e96-4693-b761-db978faf0087', 'BookTitle_2', '30df0652-0b5d-40af-86d9-cd336b836648'),
       ('5cf0a359-82e1-4ddf-9c5c-d54bb50fefe1', 'BookTitle_3', '57daec39-1f36-4248-abaf-9e12bb9e77f5');

insert into books_genres(book_id, genre_id)
values ('8b0f427f-1365-4883-8834-c6b25515b848', '9fccd731-27a2-4639-b1f6-648087ef744b'),
       ('8b0f427f-1365-4883-8834-c6b25515b848', '980fab3b-338d-45e7-83b6-29b98d1c4b02'),
       ('f7b16ec4-3e96-4693-b761-db978faf0087', '66f51d2d-e6b7-4602-8b21-31439ea1f721'),
       ('f7b16ec4-3e96-4693-b761-db978faf0087', 'dc511c5a-1436-4b96-b727-3c88b1e423d4'),
       ('5cf0a359-82e1-4ddf-9c5c-d54bb50fefe1', 'a15f63d1-8cb3-477a-a5ab-b1fc0ea00fd4'),
       ('5cf0a359-82e1-4ddf-9c5c-d54bb50fefe1', '03ce09ef-f30a-4459-867f-1705cce28da6');

insert into comments(id, text, book_id)
values ('60ebe253-6b1f-410f-b159-8f51a6026ec3', 'Comment_1_Book_1', '8b0f427f-1365-4883-8834-c6b25515b848'),
       ('cbee18e7-f448-479d-b8d7-2048c087b5a0', 'Comment_2_Book_1', '8b0f427f-1365-4883-8834-c6b25515b848'),
       ('45da304-56a5-4ff9-a982-1713a42a3c56e', 'Comment_1_Book_2', 'f7b16ec4-3e96-4693-b761-db978faf0087');
