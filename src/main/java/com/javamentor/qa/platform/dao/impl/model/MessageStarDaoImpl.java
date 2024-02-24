package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.MessageStarDao;
import com.javamentor.qa.platform.models.entity.MessageStar;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class MessageStarDaoImpl extends ReadWriteDaoImpl<MessageStar, Long> implements MessageStarDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Long> messageStarByUserId(Long userId) {
        return entityManager.createQuery(
                        "SELECT m.message.id " +
                                "FROM MessageStar m " +
                                "WHERE m.user.id = :id", Long.class)
                .setParameter("id", userId)
                .getResultList();
    }

    @Override
    @Transactional
    public void deleteMessageStarByMessageIdAndUserId(Long messageId, Long userId) {
        entityManager.createQuery("DELETE FROM MessageStar m " +
                                     "WHERE m.message.id = :messageId AND m.user.id =: userId")
                .setParameter("messageId", messageId)
                .setParameter("userId", userId)
                .executeUpdate();
    }

    @Override
    public boolean existsMessageStarByMessageIdAndUserId(Long messageId, Long userId) {
        long count = (long) entityManager.createQuery("SELECT COUNT(m) FROM MessageStar m " +
                                                         "WHERE m.message.id = :messageId AND m.user.id =: userId")
                .setParameter("messageId", messageId)
                .setParameter("userId", userId)
                .getSingleResult();
        return count > 0;
    }
}
