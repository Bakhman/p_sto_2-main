package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.ReadWriteDao;
import com.javamentor.qa.platform.dao.abstracts.model.UserChatPinDao;
import com.javamentor.qa.platform.models.entity.user.UserChatPin;
import com.javamentor.qa.platform.service.abstracts.model.UserChatPinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserChatPinServiceImpl extends ReadWriteServiceImpl<UserChatPin, Long> implements UserChatPinService {
    private final UserChatPinDao userChatPinDao;

    @Autowired
    public UserChatPinServiceImpl(UserChatPinDao userChatPinDao) {
        super(userChatPinDao);
        this.userChatPinDao = userChatPinDao;
    }

    @Override
    public List<Long> userChatPinByUserId(Long userId) {
        return userChatPinDao.userChatPinByUserId(userId);
    }
}
