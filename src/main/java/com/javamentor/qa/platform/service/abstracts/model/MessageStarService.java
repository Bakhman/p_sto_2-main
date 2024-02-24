package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.MessageStar;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MessageStarService extends ReadWriteService<MessageStar, Long> {
    List<Long> messageStarByUserId(Long userId);

    void deleteMessageStarByMessageIdAndUserId(Long messageId, Long userId);

    boolean existsMessageStarByMessageIdAndUserId(Long id, Long userId);
}
