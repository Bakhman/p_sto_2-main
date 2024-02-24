
INSERT INTO tag (id, name, description) VALUES (100,'test_tag0', 'test_description0');
INSERT INTO tag (id, name, description) VALUES (101,'test_tag1', 'test_description1');
INSERT INTO tag (id, name, description) VALUES (102,'test_tag2', 'test_description2');
INSERT INTO tag (id, name, description) VALUES (103,'test_tag3', 'test_description3');
INSERT INTO tag (id, name, description) VALUES (104,'test_tag4', 'test_description4');

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
           'password',
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

INSERT INTO question
(id, title, description, persist_date, user_id, last_redaction_date, is_deleted)
VALUES
    (100,'sometitle0', 'somedescription0', '2022-11-06 14:09:23.901275', 100,'2022-11-06 14:09:23.901275', false);
INSERT INTO question
(id, title, description, persist_date, user_id, last_redaction_date, is_deleted)
VALUES
    (101,'sometitle1', 'somedescription1', '2022-11-06 14:09:23.901275', 100,'2022-11-06 14:09:23.901275', false);
INSERT INTO question
(id, title, description, persist_date, user_id, last_redaction_date, is_deleted)
VALUES
    (102,'sometitle2', 'somedescription2', '2022-11-06 14:09:23.901275', 100,'2022-11-06 14:09:23.901275', false);
INSERT INTO question
(id, title, description, persist_date, user_id, last_redaction_date, is_deleted)
VALUES
    (103,'sometitle3', 'somedescription3', '2022-11-06 14:09:23.901275', 100,'2022-11-06 14:09:23.901275', false);
INSERT INTO question
(id, title, description, persist_date, user_id, last_redaction_date, is_deleted)
VALUES
    (104,'sometitle4', 'somedescription4', '2022-11-06 14:09:23.901275', 100,'2022-11-06 14:09:23.901275', false);

INSERT INTO question_has_tag (question_id, tag_id) VALUES (100,100);
INSERT INTO question_has_tag (question_id, tag_id) VALUES (101,100);
INSERT INTO question_has_tag (question_id, tag_id) VALUES (102,100);
INSERT INTO question_has_tag (question_id, tag_id) VALUES (103,100);
INSERT INTO question_has_tag (question_id, tag_id) VALUES (104,100);
INSERT INTO question_has_tag (question_id, tag_id) VALUES (100,101);
INSERT INTO question_has_tag (question_id, tag_id) VALUES (101,101);
INSERT INTO question_has_tag (question_id, tag_id) VALUES (102,101);
INSERT INTO question_has_tag (question_id, tag_id) VALUES (103,101);
INSERT INTO question_has_tag (question_id, tag_id) VALUES (100,102);
INSERT INTO question_has_tag (question_id, tag_id) VALUES (101,102);
INSERT INTO question_has_tag (question_id, tag_id) VALUES (102,102);
INSERT INTO question_has_tag (question_id, tag_id) VALUES (100,103);
INSERT INTO question_has_tag (question_id, tag_id) VALUES (101,103);
INSERT INTO question_has_tag (question_id, tag_id) VALUES (100,104);