package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.AnswerDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.lang.reflect.ParameterizedType;
import java.util.Optional;

@Repository
public class AnswerDaoImpl extends ReadWriteDaoImpl<Answer, Long> implements AnswerDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Boolean checkAnswerByQuestionIdAndUserId(Long questionId, Long userId) {
        return entityManager.createQuery(
                "SELECT COUNT(a)>0 " +
                        "FROM Answer a " +
                        "LEFT JOIN User u ON a.id = u.id " +
                        "WHERE a.question.id = :questionId " +
                        "AND a.user.id = :userId",
                        Boolean.class)
                .setParameter("questionId", questionId)
                .setParameter("userId", userId)
                .getSingleResult();
    }

    @Override
    public void deleteById(Long answerId) {
        entityManager.createQuery("DELETE FROM Reputation r WHERE r.answer.id = :answerId")
                .setParameter("answerId", answerId)
                .executeUpdate();
        entityManager.createQuery("DELETE FROM VoteAnswer v WHERE v.answer.id = :answerId")
                .setParameter("answerId", answerId)
                .executeUpdate();
        entityManager.createQuery("DELETE FROM Answer WHERE id = :id")
                .setParameter("id", answerId)
                .executeUpdate();

    }
    @Override
    public Optional<Answer> getById(Long id) {
        TypedQuery<Answer> answers = entityManager.createQuery("SELECT a FROM Answer a where a.id = :id and a.isDeleted = false and a.isDeletedByModerator = false",Answer.class)
                .setParameter("id",id);
        return SingleResultUtil.getSingleResultOrNull(answers);
    }
}
