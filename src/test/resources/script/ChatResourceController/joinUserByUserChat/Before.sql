
INSERT INTO chat (id, title, persist_date, chat_type, is_global, is_chat_pin) VALUES (100,
                                                                                      'some_title',
                                                                                      CURRENT_TIMESTAMP,
                                                                                      2,
                                                                                      true,
                                                                                      true);
INSERT INTO role (id, name) VALUES (101, 'ADMIN'),(102, 'USER');
INSERT INTO user_entity (id, email, password, full_name, persist_date, is_enabled, is_deleted, city, link_site, link_github, link_vk, about, image_link, last_redaction_date, nickname, role_id)
VALUES (100, 'vasya1@mail.ru', 'password1', 'user1', '2022-10-12T17:05:24.197649', true,NULL, 'user1_city', 'user1site.com', 'user1github.com', 'user1vk.com', 'test_user1','', CURRENT_TIMESTAMP, 'user1', 102),
       (101, 'vasya2@mail.ru', 'password2', 'user1', '2022-10-12T17:05:24.197649', true,NULL, 'user1_city', 'user1site.com', 'user1github.com', 'user1vk.com', 'test_user1','', CURRENT_TIMESTAMP, 'user1', 102),
       (102, 'vasya3@mail.ru', 'password3', 'user1', '2022-10-12T17:05:24.197649', true,NULL, 'user1_city', 'user1site.com', 'user1github.com', 'user1vk.com', 'test_user1','', CURRENT_TIMESTAMP, 'user1', 102),
       (103, 'vasya4@mail.ru', 'password4', 'user1', '2022-10-12T17:05:24.197649', true,NULL, 'user1_city', 'user1site.com', 'user1github.com', 'user1vk.com', 'test_user1','', CURRENT_TIMESTAMP, 'user1', 102);
INSERT INTO group_chat (chat_id,image_link, author_id) values (100, 'chat1', 100);
INSERT INTO groupchat_has_users (chat_id, user_id) VALUES (100, 103);