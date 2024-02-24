package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.MessageStar;

import java.util.List;

public interface MessageStarDao extends ReadWriteDao<MessageStar, Long> {
    List<Long> messageStarByUserId(Long userId);

    void deleteMessageStarByMessageIdAndUserId(Long messageId, Long userId);

    boolean existsMessageStarByMessageIdAndUserId(Long id, Long userId);
}
