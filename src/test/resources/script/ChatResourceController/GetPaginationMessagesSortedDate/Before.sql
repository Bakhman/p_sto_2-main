insert into role (id, name)
values (100, 'USER');


INSERT INTO user_entity (id,
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
VALUES (100,
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
        CURRENT_TIMESTAMP,
        'user100',
        100),
       (101,
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
        'null',
        CURRENT_TIMESTAMP,
        'user101',
        100);

insert into chat (id, title, persist_date, chat_type) VALUES
    (100,'','2022-8-12T17:05:24.197649',0);

INSERT INTO single_chat
    (chat_id,
    user_one_id,
    use_two_id)
VALUES
    (100,
    100,
    101);

INSERT INTO message
(id,
 message,
 last_redaction_date,
 persist_date,
 user_sender_id,
 chat_id)
VALUES (100,
        'some message 1010',
        '2022-8-12T17:05:24.197655',
        '2022-8-12T17:05:24.197655',
        100,
        100),
       (101,
        'some message 1012',
        '2022-8-12T17:05:24.197649',
        '2022-8-12T17:05:24.197656',
        100,
        100),
       (102,
        'some message 1020',
        '2022-8-12T17:05:24.197632',
        '2022-8-12T17:05:24.197657',
        101,
        100);