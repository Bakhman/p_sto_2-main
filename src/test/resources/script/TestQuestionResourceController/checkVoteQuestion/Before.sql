insert into role (id, name)
values (100, 'USER');


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
           100),
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

insert into question (id,
                      title,
                      description,
                      persist_date,
                      user_id,
                      last_redaction_date,
                      is_deleted)
values (100, 'title0', 'description0', '2022-10-24T17:46:51.936176', 100, '2022-10-26T17:46:51.936176', false),
       (101, 'title1', 'description1', '2022-10-21T17:46:51.936176', 101, '2022-10-28T17:46:51.936176', false),
       (102, 'title2', 'description2', '2022-10-23T17:46:51.936176', 102, '2022-10-29T17:46:51.936176', false),
       (103, 'title4', 'description3', '2022-10-23T17:46:51.936176', 102, '2022-10-29T17:46:51.936176', true);

insert into reputation (id, persist_date, author_id, sender_id, count, type, question_id, answer_id)
values (100, current_timestamp, 101, null, 500, 0, 100, null),
       (101, current_timestamp, 102, null, 300, 0, 101, null),
       (102, current_timestamp, 100, null, 600, 0, 102, null);