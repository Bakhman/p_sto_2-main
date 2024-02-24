INSERT INTO role (id, name)VALUES (100, 'USER');

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
        '$2a$12$dQgY.AqyPPKVFxf0PB98dOn4kK/qwKQr9w1SQuT.Xyg.LkIjBTd1q', /*Password!1*/
        'user100',
        '2022-01-12T17:05:24.197649',
        true,
        false,
        'user100_city',
        'user100site.com',
        'user100github.com',
        'user100vk.com',
        'test_user100',
        '\image_user100.jpg',
        CURRENT_TIMESTAMP,
        'user100',
        100);

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
VALUES (101,
        'user101@mail.ru',
        '$2a$12$LOHDPJc2WgzBQ0Ef8hwydegyThGFCBZWI5aWPzhBM3UiG/R6RYwvS', /*Password!2*/
        'user100',
        '2022-01-12T17:05:24.197649',
        true,
        false,
        'user100_city',
        'user100site.com',
        'user100github.com',
        'user100vk.com',
        'test_user100',
        '\image_user100.jpg',
        CURRENT_TIMESTAMP,
        'user100',
        100);

insert into chat (id,
                  title,
                  persist_date,
                  chat_type)
VALUES (100,
        'Some single chat',
        CURRENT_TIMESTAMP,
        1);

insert into chat (id,
                  title,
                  persist_date,
                  chat_type)
VALUES (102,
        'Some single chat',
        CURRENT_TIMESTAMP,
        1);

insert into single_chat (chat_id, user_one_id, use_two_id)
values (100, 100, 101);

insert into single_chat (chat_id, user_one_id, use_two_id, deleted_by_user_one, deleted_by_user_two)
values (102, 100, 101, true, false);

insert into message (id, message, last_redaction_date, persist_date, user_sender_id, chat_id)
values (101, 'Message_101', current_timestamp, current_timestamp, 101, 100);

insert into message (id, message, last_redaction_date, persist_date, user_sender_id, chat_id)
values (102, 'Message_102', current_timestamp, current_timestamp, 100, 100 );

insert into message (id, message, last_redaction_date, persist_date, user_sender_id, chat_id)
values (103, 'Message_103', current_timestamp, current_timestamp, 101, 100);

insert into message (id, message, last_redaction_date, persist_date, user_sender_id, chat_id)
values (104, 'Message_104', current_timestamp, current_timestamp, 100, 100);