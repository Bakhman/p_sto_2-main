insert into role(id,name) values (100,'USER');

insert into user_entity (id,email,password,full_name,persist_date,is_enabled,is_deleted,city,link_site,link_github,
                         link_vk,about,image_link,last_redaction_date,nickname,role_id)
values (100,'user100@mail.ru','12345','user100','2022-10-12T17:06:24.197649',true,false,'user100_city','user100@site.com',
        'user100@github.com','user100@vk.com','test_user100','null',CURRENT_TIMESTAMP ,'user100',100),

       (101,'user101@mail.ru','$2a$12$RbzrY3EwB4O/UElqKDY..e8e8t88SijJ0iVtMBE0VYsQUxV6Wtvhu','user101',
        '2022-9-12T17:05:24.197649',true,NULL,'user101_city','user101site.com','user101github.com','user101vk.com',
        'test_user101','',CURRENT_TIMESTAMP,'user101',100),

       (102,'user102@mail.ru','$2a$12$RbzrY3EwB4O/UElqKDY..e8e8t88SijJ0iVtMBE0VYsQUxV6Wtvhu','user102',
        '2022-8-12T17:05:24.197649',true,NULL,'user102_city','user102site.com','user102github.com','user102vk.com',
        'test_user102','',CURRENT_TIMESTAMP,'user102',100);

insert into chat(id,title,persist_date)
values (100,'Чат для теста с id : 100',CURRENT_TIMESTAMP),
       (101,'Чат для теста с id : 101',CURRENT_TIMESTAMP),
       (102,'Чат для теста с id : 102',CURRENT_TIMESTAMP);


insert into group_chat(chat_id,image_link)
values (101,NULL),(102,NULL);

insert into groupchat_has_users(chat_id,user_id)
values (101,100),(101,102),(101,101),
       (102,101),(102,102);


insert into message(id,message,last_redaction_date,persist_date,user_sender_id,chat_id)
values (51,'MyMessage51',CURRENT_TIMESTAMP,'2022-08-12T17:04:24.197649',100,101),
       (52,'MyMessageTwo52',CURRENT_TIMESTAMP,'2022-08-12T17:05:24.197649',102,101),
       (53,'MyMessageThree53',CURRENT_TIMESTAMP,'2022-08-12T17:06:24.197649',101,101),
       (54,'MyMessage54',CURRENT_TIMESTAMP,'2022-08-12T17:07:24.197649',102,102),
       (55,'MyMessageTwo55',CURRENT_TIMESTAMP,'2022-08-12T17:08:24.197649',102,102),
       (56,'MyMessageThree56',CURRENT_TIMESTAMP,'2022-08-12T17:09:24.197649',101,102);