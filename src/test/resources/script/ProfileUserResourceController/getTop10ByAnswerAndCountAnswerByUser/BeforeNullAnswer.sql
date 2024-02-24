insert into role (id, name)
values (100, 'ADMIN'),
       (200, 'USER');
insert into user_entity(id, email, password, is_enabled, role_id, last_redaction_date)
values (101, 'vasya@mail.ru', 'password', true, 200, current_date),
       (102, 'user2@mail.ru', '3111', true, 200, current_date);
insert into question (id, user_id, title, last_redaction_date, description)
values (101, 101, 'A', current_date, 'a'),
       (102, 102, 'A', current_date, 'a');