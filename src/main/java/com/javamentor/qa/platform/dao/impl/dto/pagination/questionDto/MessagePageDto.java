package com.javamentor.qa.platform.dao.impl.dto.pagination.questionDto;
import com.javamentor.qa.platform.dao.abstracts.dto.pagination.PaginationDtoAble;
import com.javamentor.qa.platform.models.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;


@Repository("PaginationServiceDto")
@RequiredArgsConstructor
public class MessagePageDto implements PaginationDtoAble<MessageDto> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<MessageDto> getItems(Map<String, Object> param) {
        int currentPageNumber = (int) param.get("currentPageNumber");
        int itemsOnPage = (int) param.get("itemsOnPage");
        long chatId = (long) param.get("chatId");
        String word = (String) param.get("word");
        return entityManager.createQuery(
                        "SELECT new com.javamentor.qa.platform.models.dto.MessageDto " +
                                "(m.id, " +
                                "m.message, " +
                                "u.nickname, " +
                                "u.id, " +
                                "m.chat.id, " +
                                "u.imageLink, " +
                                "m.persistDate) " +
                                "FROM Message m LEFT JOIN User u ON m.userSender.id = u.id " +
                                "WHERE m.chat.id = :chatId and m.message LIKE :word ", MessageDto.class)
                .setParameter("word", "%" + word + "%")
                .setParameter("chatId", chatId)
                .setFirstResult((currentPageNumber - 1) * itemsOnPage)
                .setMaxResults(itemsOnPage)
                .getResultList();
    }

    @Override
    public int getTotalResultCount(Map<String, Object> param) {
        long chatId = (long) param.get("chatId");
        return ((Long) entityManager.createQuery("SELECT COUNT(m) FROM Message m where m.chat.id = :chatId")
                .setParameter("chatId", chatId)
                .getSingleResult())
                .intValue();
    }

}
