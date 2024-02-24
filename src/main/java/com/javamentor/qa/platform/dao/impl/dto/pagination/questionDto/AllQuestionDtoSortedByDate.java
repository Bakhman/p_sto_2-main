package com.javamentor.qa.platform.dao.impl.dto.pagination.questionDto;

import com.javamentor.qa.platform.dao.abstracts.dto.pagination.PaginationDtoAble;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.javamentor.qa.platform.dao.impl.filter.DayFilter;
import com.javamentor.qa.platform.models.dto.QuestionViewDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@RequiredArgsConstructor
@Repository("AllQuestionDtoSortedByDate")
public class AllQuestionDtoSortedByDate implements PaginationDtoAble<QuestionViewDto> {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<QuestionViewDto> getItems(Map<String, Object> param) {
        int currentPageNumber = (int) param.get("currentPageNumber");
        int itemsOnPage = (int) param.get("itemsOnPage");
        List<Long> trackedTags = (List<Long>) param.get("trackedTags");
        List<Long> ignoredTags = (List<Long>) param.get("ignoredTags");
        DayFilter dayFilter = (DayFilter) param.get("dayFilter");
        return entityManager.createQuery(

                        "SELECT DISTINCT NEW com.javamentor.qa.platform.models.dto.QuestionViewDto" +
                                "(question.id, " +
                                "question.title, " +
                                "question.user.id, " +
                                "(SELECT COALESCE(SUM(reputation.count), 0L) FROM Reputation reputation " +
                                "WHERE question.user.id = reputation.author.id), " +
                                "question.user.fullName, " +
                                "question.user.imageLink, " +
                                "question.description, " +
                                "(SELECT COUNT(*) FROM QuestionViewed qw " +
                                "WHERE qw.question.id = question.id) as CountQuestionView," +
                                "(SELECT COUNT(*) FROM Answer answer " +
                                "WHERE answer.question.id = question.id) as CountAnswer, " +

                                "((SELECT COUNT (*) FROM VoteQuestion v " +
                                "WHERE v.vote = 'UP_VOTE' AND v.question.id = question.id) - " +
                                "(SELECT COUNT (*) FROM VoteQuestion v " +
                                "WHERE v.vote = 'DOWN_VOTE' AND v.question.id = question.id)), " +

                                "question.persistDateTime, " +
                                "question.lastUpdateDateTime, " +
                                "(SELECT DISTINCT CASE WHEN EXISTS (SELECT b.id FROM BookMarks b " +
                                "WHERE b.user.id = user.id AND b.question.id = question.id) THEN true ELSE false END as isUserBookmark " +
                                "FROM BookMarks )) " +

                                "FROM Question question " +

                                "JOIN question.tags AS tags " +
                                "LEFT JOIN question.user AS author ON author.id = question.user.id " +
                                "LEFT JOIN question.answers AS answer ON answer.question.id = question.id " +
                                "WHERE question.id IN (SELECT DISTINCT question.id FROM Question question " +
                                "JOIN question.tags tags WHERE (:trackedTags) " +
                                "IS NULL OR tags.id IN (:trackedTags) GROUP BY question.id) " +
                                "AND question.id NOT IN (SELECT DISTINCT question.id FROM Question question " +
                                "JOIN question.tags tags WHERE tags.id IN (:ignoredTags) GROUP BY question.id)" +
                                "AND question.isDeleted = false " +
                                "AND ((:dayFilter) = 0 OR question.persistDateTime >= current_date - (:dayFilter)) " +
                                "ORDER BY question.persistDateTime DESC ",
                        QuestionViewDto.class)
                .setParameter("dayFilter", dayFilter.getDay())
                .setParameter("trackedTags", trackedTags)
                .setParameter("ignoredTags", ignoredTags)
                .setFirstResult((currentPageNumber - 1) * itemsOnPage)
                .setMaxResults(itemsOnPage)
                .getResultList();

    }

    @Override
    @SuppressWarnings("unchecked")
    public int getTotalResultCount(Map<String, Object> param) {

        List<Long> ignoredTags = (List<Long>) param.get("ignoredTags");
        List<Long> trackedTags = (List<Long>) param.get("trackedTags");
        DayFilter dayFilter = (DayFilter) param.get("dayFilter");
        String query = "SELECT COUNT(DISTINCT q.id) " +
                "FROM Question q " +
                "JOIN q.tags " +
                "WHERE q.id IN (SELECT q.id From Question q LEFT OUTER JOIN q.tags tgs WHERE (:trackedTags) IS NULL OR tgs.id IN (:trackedTags)) " +
                "AND q.id NOT IN (SELECT q.id From Question q LEFT OUTER JOIN q.tags tgs WHERE tgs.id IN (:ignoredTags))" +
                "AND q.isDeleted = false ";
        String notDefaultDayFilter = "AND q.persistDateTime BETWEEN :dayFilter AND LOCALTIMESTAMP";
        if (dayFilter.name().equals("ALL")) {
            return ((Long) entityManager.createQuery(query)
                    .setParameter("trackedTags", trackedTags)
                    .setParameter("ignoredTags", ignoredTags)
                    .getSingleResult()).intValue();
        }
        return ((Long) entityManager.createQuery(query + notDefaultDayFilter)
                .setParameter("dayFilter", LocalDateTime.now().minusDays(dayFilter.getDay()))
                .setParameter("trackedTags", trackedTags)
                .setParameter("ignoredTags", ignoredTags)
                .getSingleResult()).intValue();
        }
}