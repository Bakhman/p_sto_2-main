
INSERT INTO tag (id, name, description) VALUES (100,'test_tag0', 'test_description0');
INSERT INTO tag (id, name, description) VALUES (101,'test_tag1', 'test_description1');
INSERT INTO tag (id, name, description) VALUES (102,'test_tag2', 'test_description2');
INSERT INTO role (id, name) VALUES (102, 'USER');
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
VALUES (
           100,
           'vasya@mail.ru',
           'password', /*123*/
           'user1',
           '2022-10-12T17:05:24.197649',
           true,
           NULL,
           'user1_city',
           'user1site.com',
           'user1github.com',
           'user1vk.com',
           'test_user1',
           '',
           CURRENT_TIMESTAMP,
           'user1',
           102);
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
VALUES (
           101,
           'notautorizeduser@mail.ru',
           'password', /*123*/
           'user1',
           '2022-10-12T17:05:24.197649',
           true,
           NULL,
           'user1_city',
           'user1site.com',
           'user1github.com',
           'user1vk.com',
           'test_user1',
           '',
           CURRENT_TIMESTAMP,
           'user1',
           102);
INSERT INTO tag_tracked (id, tracked_tag_id, user_id) VALUES (100,100,100);
INSERT INTO tag_tracked (id, tracked_tag_id, user_id) VALUES (101,101,100);
INSERT INTO tag_tracked (id, tracked_tag_id, user_id) VALUES (102,102,101);