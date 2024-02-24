insert into role (id, name)
values (200, 'USER');


insert into user_entity(id, email, password, is_enabled, role_id, last_redaction_date, full_name, image_link, city,
                        persist_date)
values (101, 'vasya@mail.ru', 'password', true, 200, current_date, 'Vas', 'a.b', 'city',
        '2010-10-10'),
       (103, 'vasya1@mail.ru', 'password', true, 200, current_date, 'vas1', 'a.b1', 'city1', current_date),
       (102, 'user2@mail.ru', '3111', true, 200, current_date, 'Vas2', 'a.b2', 'city2', current_date),
       (104, 'vasya4@mail.ru', 'password', true, 200, current_date, 'Vas3', 'a.b3', 'city3', current_date),
       (105, 'user5@mail.ru', '3111', true, 200, current_date, 'Vas4', 'a.b4', 'city4', current_date),
       (107, 'vasya7@mail.ru', 'password', true, 200, current_date, 'vas5', 'a.b5', 'city5', current_date),
       (108, 'user8@mail.ru', '3111', true, 200, current_date, 'vas6', 'a.b6', 'city6', current_date),
       (109, 'vasya9@mail.ru', 'password', true, 200, current_date, 'vas7', 'a.b7', 'city7', current_date),
       (110, 'user10@mail.ru', '3111', true, 200, current_date, 'vas8', 'a.b8', 'city8', current_date),
       (111, 'user11@mail.ru', '3111', true, 200, current_date, 'vas9', 'a.b9', 'city9', current_date),
       (112, 'vasya12@mail.ru', 'password', true, 200, current_date, 'vas10', 'a.b10', 'city10', current_date),
       (113, 'user13@mail.ru', '3111', true, 200, current_date, 'vas11', 'a.b11', 'city11', current_date),
       (106, 'user6@mail.ru', '3111', true, 200, current_date, 'vas12', 'a.b12', 'city12', current_date);

insert into question (id, user_id, title, last_redaction_date, description)
values (101, 101, 'A', current_date, 'a'),
       (102, 102, 'A', current_date, 'a');

insert into answer (id, user_id, question_id, html_body, persist_date, update_date, is_helpful, date_accept_time,
                    is_deleted,
                    is_deleted_by_moderator)
values (101, 101, 101, 'answer', current_date, current_date, null, current_date, false, false),
       (117, 101, 101, 'answer', current_date - interval '6 day', current_date, null, current_date, false, false),
       (118, 101, 101, 'answer', current_date - interval '7 day', current_date, null, current_date, false, false),
       (119, 101, 101, 'answer', current_date - interval '31 day', current_date, null, current_date, false, false),
       (120, 101, 101, 'answer', current_date - interval '29 day', current_date, null, current_date, false, false),
       (121, 101, 101, 'answer', current_date - interval '30 day', current_date, null, current_date, false, false),
       (122, 101, 101, 'answer', current_date - interval '364 day', current_date, null, current_date, false, false),
       (123, 101, 101, 'answer', current_date - interval '366 day', current_date, null, current_date, false, false),
       (124, 101, 102, 'answer', current_date, current_date, null, current_date, false, false),
       (125, 101, 101, 'answer', current_date, current_date, null, current_date, false, false),

       (126, 102, 102, 'answer', current_date - interval '7 day', current_date, null, current_date, false, false),
       (127, 102, 102, 'answer', current_date, current_date, null, current_date, false, false),
       (128, 102, 102, 'answer', current_date, current_date, null, current_date, false, false),
       (129, 102, 102, 'answer', current_date, current_date, null, current_date, false, false),
       (130, 102, 102, 'answer', current_date, current_date, null, current_date, false, false),
       (131, 102, 102, 'answer', current_date, current_date, null, current_date, false, false),
       (132, 102, 102, 'answer', current_date, current_date, null, current_date, false, false),
       (133, 102, 102, 'answer', current_date, current_date, null, current_date, false, false),
       (134, 102, 102, 'answer', current_date, current_date, null, current_date, false, false),

       (105, 103, 101, 'answer', current_date, current_date, null, current_date, false, false),

       (106, 104, 102, 'answer', current_date, current_date, null, current_date, false, false),
       (135, 104, 102, 'answer', current_date, current_date, null, current_date, false, false),
       (136, 104, 102, 'answer', current_date, current_date, null, current_date, false, false),
       (137, 104, 102, 'answer', current_date, current_date, null, current_date, false, false),
       (138, 104, 102, 'answer', current_date, current_date, null, current_date, false, false),
       (139, 104, 102, 'answer', current_date, current_date, null, current_date, false, false),
       (140, 104, 102, 'answer', current_date, current_date, null, current_date, false, false),
       (141, 104, 102, 'answer', current_date, current_date, null, current_date, false, false),

       (107, 105, 101, 'answer', current_date, current_date, null, current_date, false, false),
       (114, 105, 102, 'answer', current_date, current_date, null, current_date, false, false),
       (115, 105, 101, 'answer', current_date, current_date, null, current_date, false, false),
       (116, 105, 102, 'answer', current_date, current_date, null, current_date, false, false),

       (108, 106, 102, 'answer', current_date - interval '20 day', current_date, null, current_date, false, false),
       (142, 106, 102, 'answer', current_date - interval '20 day', current_date, null, current_date, false, false),

       (109, 107, 101, 'answer', current_date - interval '20 day', current_date, null, current_date, false, false),
       (143, 107, 101, 'answer', current_date - interval '20 day', current_date, null, current_date, false, false),

       (110, 108, 102, 'answer', current_date - interval '20 day', current_date, null, current_date, false, false),
       (145, 108, 102, 'answer', current_date - interval '20 day', current_date, null, current_date, false, false),

       (111, 109, 101, 'answer', current_date - interval '20 day', current_date, null, current_date, false, false),
       (146, 109, 101, 'answer', current_date - interval '20 day', current_date, null, current_date, false, false),

       (112, 110, 102, 'answer', current_date - interval '20 day', current_date, null, current_date, false, false),
       (147, 110, 102, 'answer', current_date - interval '20 day', current_date, null, current_date, false, false),

       (113, 111, 101, 'answer', current_date - interval '40 day', current_date, null, current_date, false, false);

insert into reputation(id, author_id, count, sender_id, question_id, answer_id)
values (101, 101, 5, 102, 101, null),
       (102, 101, 5, 102, null, 101),
       (103, 102, 1, 104, 102, null);

insert into votes_on_answers(id, user_id, answer_id, persist_date, vote)
values (101, 102, 101, current_date, 'UP_VOTE'),
       (102, 103, 101, current_date, 'UP_VOTE'),
       (103, 107, 126, current_date, 'UP_VOTE');