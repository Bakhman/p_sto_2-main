package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.chat.GroupChat;

import java.util.Optional;

public interface GroupChatDao  extends ReadWriteDao<GroupChat, Long>{

    void deleteUserFromGroupChatById(Long groupChatId, Long userId);
    Boolean groupChatVerifyUser(Long user_id, Long chat_id);
    boolean checkUserIsAddedToGroupChat(Long userId);
    void addUserToGroupChat(Long chatId, Long userId);
    Optional<GroupChat> getGroupChatWithUsersById(Long groupChatId);
}
