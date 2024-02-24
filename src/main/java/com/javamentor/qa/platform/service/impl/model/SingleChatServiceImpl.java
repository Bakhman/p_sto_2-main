package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.MessageDao;
import com.javamentor.qa.platform.dao.abstracts.model.SingleChatDao;
import com.javamentor.qa.platform.exception.ChatException;
import com.javamentor.qa.platform.models.entity.chat.Message;
import com.javamentor.qa.platform.models.entity.chat.SingleChat;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.SingleChatService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class SingleChatServiceImpl extends ReadWriteServiceImpl<SingleChat, Long> implements SingleChatService {

    private final SingleChatDao singleChatDao;
    private final MessageDao messageDao;
    public SingleChatServiceImpl(SingleChatDao singleChatDao, MessageDao messageDao) {
        super(singleChatDao);
        this.singleChatDao = singleChatDao;
        this.messageDao = messageDao;
    }

    @Override
    @Transactional
    public void deleteByChatId(Long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        Optional<SingleChat> singleChat = getById(id);
        if (singleChat.get().getUserOne().getId() == user.getId() && singleChat.get().isDeletedByUserOne() ||
            singleChat.get().getUseTwo().getId() == user.getId() && singleChat.get().isDeletedByUserTwo()) {
            throw new ChatException("Single Chat № " + id + " для данного пользователя уже удален.");
        }
        singleChatDao.deleteByChatId(id, user.getId());
    }

    @Override
    @Transactional
    public void addSingleChatAndMessage(SingleChat singleChat, String message) {
        User userOne = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        singleChatDao.persist(singleChat);
        messageDao.persist(new Message(message, userOne, singleChat.getChat()));
    }
}
