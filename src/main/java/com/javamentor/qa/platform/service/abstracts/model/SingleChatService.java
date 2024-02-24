package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.chat.SingleChat;
import org.springframework.stereotype.Service;

@Service
public interface SingleChatService extends ReadWriteService<SingleChat, Long>{
    void deleteByChatId(Long id);

    void addSingleChatAndMessage(SingleChat singleChat, String message);
}
