package com.javamentor.qa.platform.dao.impl.dto.pagination;

import com.javamentor.qa.platform.dao.abstracts.dto.pagination.PaginationDtoAble;
import com.javamentor.qa.platform.models.dto.CommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@Repository("AllCommentDtoByAnswerId")
@RequiredArgsConstructor
public class GetAllCommentDtoByAnswerId implements PaginationDtoAble<CommentDto> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<CommentDto> getItems(Map<String, Object> param) {
        int currentPage = (int) param.get("currentPageNumber");
        int items = (int) param.get("itemsOnPage");
        long answerId = (long) param.get("answerId");

        return entityManager.createQuery("SELECT new com.javamentor.qa.platform.models.dto.CommentDto" +
                        "(comment.id, " +
                        "comment.text, " +
                        "comment.user.id, " +
                        "comment.user.fullName, " +
                        "(SELECT COALESCE(SUM(reputation.count), 0L) FROM Reputation reputation" +
                        " WHERE reputation.author.id = comment.user.id), " +
                        "comment.persistDateTime) " +
                        "FROM Comment comment " +
                        "JOIN CommentAnswer ca ON (comment.id = ca.comment.id) " +
                        "WHERE ca.answer.id = :answerId ORDER BY comment.persistDateTime DESC" , CommentDto.class)
                .setParameter("answerId", answerId)
                .setFirstResult((currentPage - 1) * items)
                .setMaxResults(items)
                .getResultList();
    }

    @Override
    public int getTotalResultCount(Map<String, Object> param) {
        long answerId = (long) param.get("answerId");
        return ((Long) entityManager.createQuery(
                        "SELECT COUNT(comment) FROM Comment comment " +
                                "JOIN CommentAnswer ca ON (comment.id = ca.comment.id) " +
                                "WHERE ca.answer.id = :answerId")
                .setParameter("answerId", answerId)
                .getSingleResult()).intValue();
    }
}