package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.user.UserChatPin;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserChatPinService extends ReadWriteService<UserChatPin, Long> {
    List<Long> userChatPinByUserId (Long userId);
}
