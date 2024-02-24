package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.GroupChatDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.chat.GroupChat;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class GroupChatDaoImpl extends ReadWriteDaoImpl<GroupChat, Long> implements GroupChatDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void deleteUserFromGroupChatById(Long groupChatId, Long userId) {
        entityManager.createNativeQuery("DELETE FROM groupchat_has_users WHERE chat_id = :groupChatId " +
                        "AND user_id = :userId")
                .setParameter("groupChatId", groupChatId)
                .setParameter("userId", userId)
                .executeUpdate();
    }

    @Override
    public Optional<GroupChat> getGroupChatWithUsersById(Long groupChatId) {
        TypedQuery<GroupChat> query = entityManager.createQuery("SELECT gr FROM GroupChat gr " +
                "LEFT JOIN FETCH gr.users WHERE gr.id = :groupChatId", GroupChat.class)
                .setParameter("groupChatId",groupChatId);
        return SingleResultUtil.getSingleResultOrNull(query);
    }

    @Override
    public Boolean groupChatVerifyUser(Long userId, Long chatId){
        Long count = entityManager.createQuery("select count(gc) from GroupChat gc join gc.users as user where gc.chat.id = :chatId " +
                        "and user.id in :userId",Long.class)
                .setParameter("chatId",chatId)
                .setParameter("userId",userId)
                .getSingleResult();
        return count > 0;
    }

    public boolean checkUserIsAddedToGroupChat(Long userId) {
        return entityManager
                .createQuery("select count(gc) from GroupChat gc join gc.users as user" + " where user.id = :userId",Long.class)
                .setParameter("userId", userId)
                .getSingleResult() > 0;
    }

    @Override
    public void addUserToGroupChat(Long chatId, Long userId) {
        entityManager
                .createNativeQuery("INSERT INTO groupchat_has_users (chat_id, user_id) VALUES (?,?)")
                .setParameter(1, chatId)
                .setParameter(2, userId)
                .executeUpdate();
    }

}