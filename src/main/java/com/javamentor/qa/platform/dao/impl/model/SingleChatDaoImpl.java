package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.SingleChatDao;
import com.javamentor.qa.platform.models.entity.chat.SingleChat;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class SingleChatDaoImpl extends ReadWriteDaoImpl<SingleChat, Long> implements SingleChatDao {

    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public void deleteByChatId(Long id, Long userId) {
        entityManager.createQuery(
                        "UPDATE SingleChat SET " +
                                "deletedByUserOne = CASE WHEN userOne.id = :userId THEN true ELSE deletedByUserOne END, " +
                                "deletedByUserTwo = CASE WHEN useTwo.id = :userId THEN true ELSE deletedByUserTwo END " +
                                "WHERE chat.id = :id")
                .setParameter("id", id)
                .setParameter("userId", userId)
                .executeUpdate();
    }
}
