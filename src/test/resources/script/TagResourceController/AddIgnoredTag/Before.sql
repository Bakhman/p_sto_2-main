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
        100);

INSERT INTO tag (id, name, description, persist_date)
VALUES
    (100,
     'some tag 100',
     'some description',
     '2022-8-12T17:05:24.197649'),
    (101,
     'some tag 101',
     'some description',
     '2022-8-12T17:05:24.197649'),
    (102,
     'some tag 102',
     'some description',
     '2022-8-12T17:05:24.197649');

INSERT INTO tag_tracked (id, tracked_tag_id, user_id, persist_date)
VALUES
    (100,
     100,
     100,
     '2022-8-12T17:05:24.197649');

INSERT INTO tag_ignore (id, ignored_tag_id, user_id, persist_date)
VALUES
    (100,
     101,
     100,
     '2022-8-12T17:05:24.197649')