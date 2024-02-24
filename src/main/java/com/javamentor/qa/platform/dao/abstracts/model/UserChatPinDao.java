package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.user.UserChatPin;

import java.util.List;

public interface UserChatPinDao extends ReadWriteDao<UserChatPin, Long>{
    List<Long> userChatPinByUserId(Long userId);
}
