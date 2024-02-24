package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.chat.SingleChat;

public interface SingleChatDao extends ReadWriteDao<SingleChat, Long>{

    void deleteByChatId(Long id, Long userId);
}
