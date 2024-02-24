insert into role (id,name) values (100,'USER');

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



INSERT INTO question(id,user_id,last_redaction_date)
VALUES (100,101,'2022-08-12T17:05:24.197649'),(200,101,current_timestamp);

INSERT INTO answer (id,update_date,question_id,user_id,is_deleted,is_deleted_by_moderator,is_helpful)
VALUES (100,CURRENT_TIMESTAMP ,100,102,false,false,false),
       (200,CURRENT_TIMESTAMP,100,101,true,false,false),
       (300,CURRENT_TIMESTAMP,100,101,false,true,false);

INSERT INTO reputation (id, persist_date, author_id, sender_id, count, type, question_id, answer_id)
VALUES (201, current_timestamp, 101, null, 500, 1, null, 100),
       (202, current_timestamp, 101, null, 300, 1, null, 100),
       (203, current_timestamp, 101, null, 200, 1, null, 100);


INSERT INTO comment (id, text, comment_type, persist_date, last_redaction_date, user_id)
VALUES (100, 'MessageOne!', '1', '2022-08-12T17:01:24.197649', current_timestamp, 101),
       (101, 'MessageTwo!', '1', '2022-08-12T17:02:24.197649', current_timestamp, 100),
       (102, 'MessageThree!', '1','2022-08-12T17:03:24.197649', current_timestamp, 101),
       (103, 'MessageFor!', '1', '2022-08-12T17:04:24.197649', current_timestamp, 102),
       (104, 'MessageFive!', '1', '2022-08-12T17:05:24.197649', current_timestamp, 102);

INSERT INTO comment_answer (comment_id, answer_id)
VALUES (100, 100),
       (101, 100),
       (102, 100),
       (103, 100),
       (104, 100);