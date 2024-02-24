package com.javamentor.qa.platform.dao.impl.dto.pagination.questionDto;

import com.javamentor.qa.platform.dao.abstracts.dto.pagination.PaginationDtoAble;
import com.javamentor.qa.platform.dao.impl.filter.DayFilter;
import com.javamentor.qa.platform.models.dto.QuestionViewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository("AllQuestionDtoByVoitAndAnswerAndView")
@RequiredArgsConstructor
public class AllQuestionsByVoitAndAnswerAndView implements PaginationDtoAble<QuestionViewDto> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<QuestionViewDto> getItems(Map<String, Object> param) {
        int currentPageNumber = (int) param.get("currentPageNumber");
        int itemsOnPage = (int) param.get("itemsOnPage");
        DayFilter dayFilter = (DayFilter) param.get("dayFilter");
        return entityManager.createQuery(
                        "SELECT DISTINCT new com.javamentor.qa.platform.models.dto.QuestionViewDto" +
                                "(question.id, " +
                                "question.title, " +
                                "author.id, " +
                                "(SELECT COALESCE(SUM(reputation.count), 0L) FROM Reputation reputation " +
                                "WHERE reputation.author.id = author.id) , " +
                                "author.fullName, " +
                                "author.imageLink, " +
                                "question.description, " +

                                "(SELECT COUNT(questionViewed.question.id) FROM QuestionViewed questionViewed " +
                                "WHERE questionViewed.question.id = question.id) as countQuestionView, " +
                                "(SELECT COUNT (*) FROM Answer answer " +
                                "WHERE answer.question.id = question.id) AS countAnswer, " +

                                "((SELECT COUNT (*) FROM VoteQuestion voteOnQuestion " +
                                "WHERE voteOnQuestion.vote = 'UP_VOTE' " +
                                "AND voteOnQuestion.question.id = question.id) - " +
                                "(SELECT COUNT (*) FROM VoteQuestion voteOnQuestion " +
                                "WHERE voteOnQuestion.vote = 'DOWN_VOTE' " +
                                "AND voteOnQuestion.question.id = question.id)) AS countVoute, " +

                                "question.persistDateTime, " +
                                "question.lastUpdateDateTime," +
                                "(SELECT DISTINCT  CASE WHEN EXISTS (SELECT  b.id FROM BookMarks b " +
                                "WHERE b.user.id = author.id AND b.question.id = question.id) " +
                                "THEN true ELSE false END as isUserBookmark FROM BookMarks ) )" +

                                "FROM Question question " +

                                "LEFT JOIN question.user AS author ON (question.user.id = author.id) " +
                                "LEFT JOIN question.answers AS answer ON (question.id = answer.question.id) " +
                                "WHERE question.isDeleted = false AND " +
                                "((:dayFilter) = 0  OR question.persistDateTime >= current_date - (:dayFilter)) " +
                                "ORDER BY countVoute desc, countAnswer desc, countQuestionView desc",
                        QuestionViewDto.class)
                .setParameter("dayFilter", dayFilter.getDay())
                .getResultStream()
                .skip((long) itemsOnPage * (currentPageNumber - 1))
                .limit(itemsOnPage)
                .collect(Collectors.toList());
    }

    @Override
    public int getTotalResultCount(Map<String, Object> param) {
        DayFilter dayFilter = (DayFilter) param.get("dayFilter");
        String query = "SELECT COUNT(question.id) FROM Question question " +
                "WHERE question.isDeleted = false ";
        String notDefaultDayFilter = "AND question.persistDateTime BETWEEN :dayFilter and LOCALTIMESTAMP";
        if (dayFilter.name().equals("ALL")) {
            return ((Long) entityManager.createQuery(query).getSingleResult()).intValue();
        }
        return ((Long) entityManager.createQuery(query + notDefaultDayFilter)
                .setParameter("dayFilter", LocalDateTime.now().minusDays(dayFilter.getDay()))
                .getSingleResult()).intValue();
    }
}