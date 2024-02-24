package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.UserChatPinDao;
import com.javamentor.qa.platform.models.entity.user.UserChatPin;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserChatPinDaoImpl extends ReadWriteDaoImpl<UserChatPin, Long> implements UserChatPinDao {
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<Long> userChatPinByUserId(Long userId) {
        return entityManager.createQuery("SELECT p.chat.id " +
                "FROM UserChatPin p " +
                "WHERE p.user.id = :id",
                Long.class)
                .setParameter("id", userId)
                .getResultList();
    }
}
