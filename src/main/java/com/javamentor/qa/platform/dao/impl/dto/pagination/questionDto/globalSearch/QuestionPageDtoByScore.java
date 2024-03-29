package com.javamentor.qa.platform.dao.impl.dto.pagination.questionDto.globalSearch;

import com.javamentor.qa.platform.dao.abstracts.dto.pagination.PaginationDtoAble;
import com.javamentor.qa.platform.models.dto.QuestionViewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository("QuestionPageDtoByScore")
@RequiredArgsConstructor
public class QuestionPageDtoByScore implements PaginationDtoAble<QuestionViewDto> {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<QuestionViewDto> getItems(Map<String, Object> param) {
        long score1 = (long) param.get("score1");
        long score2 = (long) param.get("score2");
        int currentPageNumber = (int) param.get("currentPageNumber");
        int itemsOnPage = (int) param.get("itemsOnPage");


        return entityManager.createQuery("SELECT DISTINCT new com.javamentor.qa.platform.models.dto.QuestionViewDto" +
                        "(question.id, " +
                        "question.title, " +
                        "author.id, " +
                        "(SELECT COALESCE(SUM(reputation.count), 0L) FROM Reputation reputation WHERE reputation.author.id = author.id), " +
                        "author.fullName, " +
                        "author.imageLink, " +
                        "question.description, " +
                        "(SELECT COUNT(questionViewed.question.id) FROM QuestionViewed questionViewed WHERE questionViewed.question.id = question.id), " +
                        "(SELECT COUNT(*) FROM Answer answer WHERE answer.question.id = question.id), " +
                        "((SELECT COUNT(*) FROM VoteQuestion voteOnQuestion " +
                        "WHERE voteOnQuestion.vote = 'UP_VOTE' AND voteOnQuestion.question.id = question.id) - " +
                        "(SELECT COUNT(*) FROM VoteQuestion voteOnQuestion " +
                        "WHERE voteOnQuestion.vote = 'DOWN_VOTE' AND voteOnQuestion.question.id = question.id)) AS vot, " +
                        "question.persistDateTime, " +
                        "question.lastUpdateDateTime, " +
                        "(SELECT DISTINCT  CASE WHEN EXISTS (SELECT  b.id FROM BookMarks b WHERE b.user.id = author.id AND b.question.id = question.id) THEN true ELSE false END as isUserBookmark FROM BookMarks ) )" +
                        "FROM Question question " +
                        "LEFT OUTER JOIN question.user AS author ON (question.user.id = author.id) " +
                        "LEFT OUTER JOIN question.answers AS answer ON (question.id = answer.question.id) " +
                        "LEFT JOIN question.user WHERE " +
                        "(SELECT COUNT(*) FROM VoteQuestion voteOnQuestion " +
                        "WHERE voteOnQuestion.vote = 'UP_VOTE' AND voteOnQuestion.question.id = question.id) - "+
                        "(SELECT COUNT(*) FROM VoteQuestion voteOnQuestion " +
                        "WHERE voteOnQuestion.vote = 'DOWN_VOTE' AND voteOnQuestion.question.id = question.id)"+
                        " >= :score1 AND " +
                        "(SELECT COUNT(*) FROM VoteQuestion voteOnQuestion " +
                        "WHERE voteOnQuestion.vote = 'UP_VOTE' AND voteOnQuestion.question.id = question.id) - "+
                        "(SELECT COUNT(*) FROM VoteQuestion voteOnQuestion " +
                        "WHERE voteOnQuestion.vote = 'DOWN_VOTE' AND voteOnQuestion.question.id = question.id)"+
                        " <= :score2 AND question.isDeleted = false ORDER BY question.id", QuestionViewDto.class)
                .setParameter("score1",(long) score1)
                .setParameter("score2",(long) score2)
                .getResultStream()
                .skip((long) (currentPageNumber - 1) * itemsOnPage)
                .limit(itemsOnPage)
                .collect(Collectors.toList());
    }


    @Override
    public int getTotalResultCount(Map<String, Object> param) {
        long score1 = (long) param.get("score1");
        long score2 = (long) param.get("score2");

        return Math.toIntExact((Long) entityManager.createQuery("SELECT COUNT(DISTINCT question.id) FROM Question question LEFT JOIN question.user " +
                        "WHERE " +
                        "(SELECT COUNT(*) FROM VoteQuestion voteOnQuestion " +
                        "WHERE voteOnQuestion.vote = 'UP_VOTE' AND voteOnQuestion.question.id = question.id) - "+
                        "(SELECT COUNT(*) FROM VoteQuestion voteOnQuestion " +
                        "WHERE voteOnQuestion.vote = 'DOWN_VOTE' AND voteOnQuestion.question.id = question.id)"+
                        " >= :score1 AND " +
                        "(SELECT COUNT(*) FROM VoteQuestion voteOnQuestion " +
                        "WHERE voteOnQuestion.vote = 'UP_VOTE' AND voteOnQuestion.question.id = question.id) - "+
                        "(SELECT COUNT(*) FROM VoteQuestion voteOnQuestion " +
                        "WHERE voteOnQuestion.vote = 'DOWN_VOTE' AND voteOnQuestion.question.id = question.id)"+
                        " <= :score2 AND question.isDeleted = false")
                .setParameter("score1",(long) score1)
                .setParameter("score2",(long) score2)
                .getSingleResult());
    }
}