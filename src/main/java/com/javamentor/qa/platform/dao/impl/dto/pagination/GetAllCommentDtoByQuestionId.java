package com.javamentor.qa.platform.dao.impl.dto.pagination;


import com.javamentor.qa.platform.dao.abstracts.dto.pagination.PaginationDtoAble;
import com.javamentor.qa.platform.models.dto.CommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@Repository("AllCommentsByQuestionId")
@RequiredArgsConstructor
public class GetAllCommentDtoByQuestionId implements PaginationDtoAble<CommentDto> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<CommentDto> getItems(Map<String, Object> param) {
        int currentPageNumber = (int) param.get("currentPageNumber");
        int itemsOnPage = (int) param.get("itemsOnPage");
        long questionId = (long) param.get("questionId");

        return entityManager.createQuery(
                "SELECT new com.javamentor.qa.platform.models.dto.CommentDto" +
                        "(comment.id, " +
                        "comment.text, " +
                        "comment.user.id, " +
                        "comment.user.fullName, " +
                        "(SELECT COALESCE(SUM(reputation.count), 0L) FROM Reputation reputation WHERE reputation.author.id = comment.user.id), " +
                        "comment.persistDateTime) " +
                        "FROM Comment comment " +
                        "JOIN CommentQuestion cq ON (comment.id = cq.comment.id) " +
                        "WHERE cq.question.id = :questionId " +
                        "ORDER BY comment.persistDateTime DESC", CommentDto.class)

                .setParameter("questionId", questionId)
                .setFirstResult((currentPageNumber - 1) * itemsOnPage)
                .setMaxResults(itemsOnPage)
                .getResultList();

    }

    @Override
    public int getTotalResultCount(Map<String, Object> param) {
        long questionId = (long) param.get("questionId");
        return ((Long) entityManager.createQuery(
                "SELECT COUNT(comment) FROM Comment comment " +
                        "JOIN CommentQuestion cq ON (comment.id = cq.comment.id) " +
                        "WHERE cq.question.id = :questionId")
                .setParameter("questionId", questionId)
                .getSingleResult()).intValue();
    }
}
