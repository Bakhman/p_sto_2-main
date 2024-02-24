insert into role (id, name)
values (200, 'USER');

insert into user_entity(id, email, password, is_enabled, role_id, last_redaction_date, full_name, image_link, city,
                        persist_date)
values (101, 'vasya@mail.ru', 'password', true, 200, current_date, 'VasVas', 'a.b', 'city', current_date),
       (103, 'vasya1@mail.ru', 'password', true, 200, current_date, 'vas1Vas', 'a.b1', 'city1', current_date),
       (102, 'user2@mail.ru', '3111', true, 200, current_date, 'Vas2Vas', 'a.b2', 'city2', current_date),
       (104, 'vasya4@mail.ru', 'password', true, 200, current_date, 'Vas3Vas', 'a.b3', 'city3', current_date),
       (105, 'user5@mail.ru', '3111', true, 200, current_date, 'Vas4Vas', 'a.b4', 'city4', current_date);

insert into question (id, user_id, title, last_redaction_date, persist_date, description, is_deleted)
values (101, 101, 'Question 101', current_date, current_date - interval '7 day', 'description 1', false),
       (102, 102, 'Question 102', current_date, current_date - interval '8 day', 'description 2', false),
       (103, 101, 'Question 103', current_date, current_date - interval '9 day', 'description 3', false),
       (104, 102, 'Question 104', current_date, current_date, 'description 4', false),
       (105, 101, 'Question 105', current_date, current_date, 'description 5', false),
       (106, 102, 'Question 106', current_date, current_date, 'description 6', false),
       (107, 102, 'Question 107', current_date, current_date, 'description 7', false),
       (108, 102, 'Question 108', current_date, current_date, 'description 8', false),
       (109, 102, 'Question 109', current_date, current_date, 'description 9', false),
       (110, 102, 'Question 110', current_date, current_date - interval '30 day', 'description 10', false),
       (111, 101, 'Question 111', current_date, current_date - interval '31 day', 'description 11', false);


insert into question_viewed (id, user_id, question_id, persist_date)
values (101, 101, 101, current_date),
       (102, 102, 102, current_date);


insert into bookmarks (id, user_id, question_id, persist_date)
values (101, 101, 101, current_date),
       (102, 102, 102, current_date);



insert into answer (id, user_id, question_id, html_body, persist_date, update_date, is_helpful, date_accept_time,
                    is_deleted, is_deleted_by_moderator)
values (101, 101, 101, 'answer', current_date, current_date, null, current_date, false, false),
       (117, 102, 101, 'answer', current_date, current_date, null, current_date, false, false),
       (118, 103, 101, 'answer', current_date, current_date, null, current_date, false, false),

       (119, 101, 102, 'answer', current_date, current_date, null, current_date, false, false),
       (120, 102, 102, 'answer', current_date, current_date, null, current_date, false, false),
       (121, 103, 102, 'answer', current_date, current_date, null, current_date, false, false),
       (122, 104, 102, 'answer', current_date, current_date, null, current_date, false, false),
       (123, 105, 102, 'answer', current_date, current_date, null, current_date, false, false),
       (124, 104, 102, 'answer', current_date, current_date, null, current_date, false, false),
       (125, 103, 102, 'answer', current_date, current_date, null, current_date, false, false),
       (126, 102, 102, 'answer', current_date, current_date, null, current_date, false, false),
       (127, 102, 102, 'answer', current_date, current_date, null, current_date, false, false),
       (128, 102, 102, 'answer', current_date, current_date, null, current_date, false, false);


insert into reputation (id, author_id, count, sender_id, question_id, answer_id)
values (101, 101, 5, 102, 101, null),
       (102, 101, 5, 102, null, 101),
       (103, 102, 1, 104, 102, null);

insert into votes_on_questions (id, user_id, question_id, persist_date, vote)
values (101, 101, 101, current_date, 'UP_VOTE'),
       (102, 102, 101, current_date, 'UP_VOTE'),
       (103, 105, 102, current_date, 'UP_VOTE'),
       (104, 104, 102, current_date, 'UP_VOTE'),
       (105, 101, 102, current_date, 'UP_VOTE'),
       (106, 103, 102, current_date, 'UP_VOTE'),
       (107, 103, 101, current_date, 'UP_VOTE'),
       (108, 104, 101, current_date, 'UP_VOTE'),
       (109, 105, 101, current_date, 'UP_VOTE');


insert into tag (id, name, description, persist_date)
values (101, 'tag 1', 'description tag 1', current_date),
       (102, 'tag 2', 'description tag 2', current_date),
       (103, 'tag 3', 'description tag 3', current_date),
       (104, 'tag 3', 'description tag 4', current_date),
       (105, 'tag 5', 'description tag 5', current_date),
       (106, 'tag 6', 'description tag 6', current_date);

insert into question_has_tag (question_id, tag_id)
values (101, 102),
       (101, 101),

       (102, 101),
       (103, 104),
       (104, 105),
       (105, 104),
       (106, 101),
       (107, 104),
       (108, 105),
       (109, 104),
       (110, 106);
