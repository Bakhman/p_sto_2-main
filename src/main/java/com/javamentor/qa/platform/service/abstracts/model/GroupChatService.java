package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.chat.GroupChat;
import org.springframework.stereotype.Service;

@Service
public interface GroupChatService extends ReadWriteService<GroupChat, Long>{

    void deleteGroupChatByChatId(Long id);

    void addUserToGroupChat(Long chatId, Long userId);

    void deleteUserFromGroupChatById(Long groupChatId, Long userId);
    Boolean chatVerifyUser(Long user_id, Long chat_id);

}
