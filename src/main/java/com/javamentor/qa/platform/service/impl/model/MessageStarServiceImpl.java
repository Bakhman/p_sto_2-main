package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.MessageStarDao;
import com.javamentor.qa.platform.models.entity.MessageStar;
import com.javamentor.qa.platform.service.abstracts.model.MessageStarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageStarServiceImpl extends ReadWriteServiceImpl<MessageStar, Long> implements MessageStarService {
    private final MessageStarDao messageStarDao;

    @Autowired
    public MessageStarServiceImpl(MessageStarDao messageStarDao) {
        super(messageStarDao);
        this.messageStarDao = messageStarDao;
    }

    @Override
    public List<Long> messageStarByUserId(Long userId) {
        return messageStarDao.messageStarByUserId(userId);
    }

    @Override
    public void deleteMessageStarByMessageIdAndUserId(Long messageId, Long userId) {
        messageStarDao.deleteMessageStarByMessageIdAndUserId(messageId, userId);
    }

    @Override
    public boolean existsMessageStarByMessageIdAndUserId(Long messageId, Long userId) {
        return messageStarDao.existsMessageStarByMessageIdAndUserId(messageId, userId);
    }
}
