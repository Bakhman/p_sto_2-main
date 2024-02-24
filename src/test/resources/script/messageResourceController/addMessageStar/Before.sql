insert into role (id, name)
values (200, 'USER');

insert into chat (id, title, persist_date, chat_type, is_global, is_chat_pin)
values (101, 'A', current_date, 1, true, true),
       (102, 'S', current_date, 1, false, false),
       (103, 'Si', current_date, 2, false, true);

insert into user_entity(id, email, password, is_enabled, role_id, last_redaction_date, nickName, image_link, city,
                        persist_date)
values (101, 'vasya@mail.ru', 'password', true, 200, current_date, 'Vas', 'a.b', 'city',
        current_date),
       (103, 'vasya1@mail.ru', 'password', true, 200, current_date, 'vas1', 'a.b1', 'city1', current_date),
       (102, 'user2@mail.ru', '3111', true, 200, current_date, 'Vas2', 'a.b2', 'city2', current_date),
       (104, 'vasya4@mail.ru', 'password', true, 200, current_date, 'Vas3', 'a.b3', 'city3', current_date),
       (105, 'user5@mail.ru', '3111', true, 200, current_date, 'Vas4', 'a.b4', 'city4', current_date);

insert into message (id, message, last_redaction_date, persist_date, user_sender_id, chat_id)
values (101, 'как найти message44?', current_date, '2010-10-10', 101, 101),
       (102, 'как найти ?', current_date, current_date - interval '5 day', 104, 101),
       (103, 'как найти true?', current_date, current_date - interval '5 day', 103, 101),
       (104, 'как найти properties?', current_date, current_date - interval '5 day', 104, 101),
       (105, 'как найти dataSource?', current_date, current_date - interval '5 day', 102, 101),
       (106, 'как найти message55?', current_date, current_date - interval '5 day', 104, 101),
       (107, 'как найти message45', current_date, current_date - interval '5 day', 102, 101),
       (109, 'как найти message45', current_date, current_date - interval '5 day', 105, 101),
       (110, 'как найти message46', current_date, current_date - interval '5 day', 104, 101),
       (111, 'как найти message47', current_date, current_date - interval '5 day', 101, 101),
       (112, 'как найти message48', current_date, '2015-05-05', 104, 101),
       (113, 'как найти message4', current_date, current_date - interval '5 day', 101, 101),
       (114, 'как найти message49', current_date, current_date - interval '5 day', 104, 101),
       (115, 'как ?', current_date, current_date - interval '5 day', 101, 101),
       (116, 'найти ?', current_date, current_date - interval '5 day', 104, 101),

       (108, 'как найти message44', current_date, current_date - interval '5 day', 104, 102),
       (120, 'как ?', current_date, current_date - interval '5 day', 101, 102);

insert into message_star(id, user_id, message_id, persist_date)
values(101, 101, 120, current_date);