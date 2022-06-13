delete from simple.message;

insert into simple.message(id, text, tag, user_id) values
(1, 'text1', 'tag1', 1),
(2, 'text2', 'tag2', 1),
(3, 'text3', 'tag1', 1),
(4, 'text4', 'tag4', 2);

alter sequence hibernate_sequence restart with 5;