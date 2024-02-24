insert into role (id, name)
values (100, 'USER');



-- insert into user_entity (id, email, password, full_name, persist_date, is_enabled, is_deleted, city, link_site, link_github, link_vk, about, image_link, last_redaction_date, nickname, role_id)
-- values (100, 'user100@mail.ru', 'password', 'Ivan', current_timestamp, false, false, 'user100City', 'someLink.com', 'github.com', 'vk.com/ivan', 'Hello! My name is Ivan!' , null, current_timestamp, 'VANYA', 2),
--        (101, 'user101@mail.ru', 'password', 'Sasha', current_timestamp, false, false, 'user101City', 'someLinkS.com', 'githubS.com', 'vk.com/sasha', 'Hello! My name is SASHA!', null, current_timestamp, 'SASHA', 2);

INSERT INTO user_entity (
    id,
    email,
    password,
    full_name,
    persist_date,
    is_enabled,
    is_deleted,
    city,
    link_site,
    link_github,
    link_vk,
    about,
    image_link,
    last_redaction_date,
    nickname,
    role_id)
VALUES (
           100,
           'user100@mail.ru',
           '12345',
           'user100',
           '2022-10-12T17:06:24.197649',
           true,
           false,
           'user100_city',
           'user100@site.com',
           'user100@github.com',
           'user100@vk.com',
           'test_user100',
           'null',
           CURRENT_TIMESTAMP ,
           'user100',
           100
       ),
       (
           101,
           'user101@mail.ru',
           '$2a$12$RbzrY3EwB4O/UElqKDY..e8e8t88SijJ0iVtMBE0VYsQUxV6Wtvhu',
           'user101',
           '2022-9-12T17:05:24.197649',
           true,
           NULL,
           'user101_city',
           'user101site.com',
           'user101github.com',
           'user101vk.com',
           'test_user101',
           '',
           CURRENT_TIMESTAMP,
           'user101',
           100),

       (
           102,
           'user102@mail.ru',
           '$2a$12$RbzrY3EwB4O/UElqKDY..e8e8t88SijJ0iVtMBE0VYsQUxV6Wtvhu',
           'user102',
           '2022-8-12T17:05:24.197649',
           true,
           NULL,
           'user102_city',
           'user102site.com',
           'user102github.com',
           'user102vk.com',
           'test_user102',
           '',
           CURRENT_TIMESTAMP,
           'user102',
           100);

INSERT INTO question(
    id, user_id, last_redaction_date)
VALUES (100, 100, '2022-8-12T17:05:24.197649'), (101, 101, '2022-8-12T17:05:24.197649'),
       (102, 101, '2022-8-12T17:05:24.197649'),  (103, 102, '2022-8-12T17:05:24.197649');


INSERT INTO bookmarks (user_id, question_id, persist_date)
VALUES (100,100,'2022-8-12T17:05:24.197649');