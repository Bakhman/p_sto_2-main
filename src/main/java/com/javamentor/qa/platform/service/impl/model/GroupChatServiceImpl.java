package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.GroupChatDao;
import com.javamentor.qa.platform.dao.abstracts.model.UserDao;
import com.javamentor.qa.platform.exception.ChatException;
import com.javamentor.qa.platform.exception.UserAlreadyAddedToGroupchatException;
import com.javamentor.qa.platform.models.entity.chat.GroupChat;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.GroupChatService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
public class GroupChatServiceImpl extends ReadWriteServiceImpl<GroupChat, Long> implements GroupChatService {

    private final GroupChatDao groupChatDao;
    private final UserDao userDao;

    public GroupChatServiceImpl(GroupChatDao groupChatDao, UserDao userDao) {
        super(groupChatDao);
        this.groupChatDao = groupChatDao;
        this.userDao = userDao;
    }

    @Override
    @Transactional
    public void deleteGroupChatByChatId(Long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        Optional<GroupChat> groupChat = getById(id);
        Set<User> users = groupChat.get().getUsers();
        if (!users.stream().anyMatch(i -> i.getId() == user.getId())) {
            throw new ChatException("Group Chat № " + id + " для данного пользователя уже удален.");
        }
        users.removeIf(i -> i.getId() == user.getId());
        groupChatDao.update(groupChat.get());

    }

    @Override
    @Transactional
    public void addUserToGroupChat(Long chatId, Long userId) {
        if (groupChatDao.checkUserIsAddedToGroupChat(userId)) {
            throw new UserAlreadyAddedToGroupchatException("User was already added to groupchat");
        }
        groupChatDao.addUserToGroupChat(chatId, userId);
    }


    @Override
    @Transactional
    public void deleteUserFromGroupChatById(Long groupChatId, Long userId) {
        User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        Optional<GroupChat> groupChat = groupChatDao.getGroupChatWithUsersById(groupChatId);
        Set<User> users = groupChat.get().getUsers();
        if (!users.stream().anyMatch(user -> user.getId().equals(userId))) {
            throw new ChatException("Group Chat " + groupChatId + " not include this user");
        }
        if (!(authUser.getId().equals(userId) || groupChat.get().getAuthor().equals(authUser))) {
            throw new ChatException("Only group chat Author can delete user");
        }
        groupChatDao.deleteUserFromGroupChatById(groupChatId, userId);
    }

    @Transactional
    @Override
    public Boolean chatVerifyUser(Long user_id, Long chat_id) {
        return groupChatDao.groupChatVerifyUser(user_id, chat_id);
    }
}
