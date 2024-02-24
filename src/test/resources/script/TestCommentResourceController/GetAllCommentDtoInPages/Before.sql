insert into role (id, name)
values (1, 'ADMIN'),
       (2, 'USER');


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
           2
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
           2),

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
           2);

INSERT INTO question(
    id, user_id, last_redaction_date)
VALUES (100, 100, '2022-8-12T17:05:24.197649'),
       (101, 100, '2022-8-12T17:06:24.197649'),
       (102, 100, '2022-8-12T17:07:24.197649');


insert into reputation (id, persist_date, author_id, sender_id, count, type, question_id, answer_id)
values (100, current_timestamp, 101, null, 500, 0, 100, null),
       (101, current_timestamp, 102, null, 300, 0, 101, null),
       (102, current_timestamp, 100, null, 600, 0, 102, null);



insert into comment (id, text, comment_type, persist_date, last_redaction_date, user_id)
values (100, 'It is cool!', '1', '2022-08-12T17:05:01.197649', null, 101),
       (101, 'It is nice!', '1', '2022-08-12T17:05:03.197649', null, 102),
       (102, 'It is beautiful!', '1', '2022-08-12T17:05:15.197649', null, 100),
       (103, 'It is dangerous!', '1', '2022-08-12T17:05:18.197649', null, 101),
       (104, 'It is awful!', '1', '2022-08-12T17:05:20.197649', null, 100),
       (105, 'It is great!', '1', '2022-08-12T17:05:21.197649', null, 102),
       (106, 'It is wonderful!', '1', '2022-08-12T17:22:46.197649', null, 101);


insert into comment_question (comment_id, question_id)
values (100, 100),
       (101, 100),
       (102, 100),
       (103, 100),
       (104, 100),
       (105, 100),
       (106, 100);


