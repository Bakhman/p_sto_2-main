package com.javamentor.qa.platform.dao.impl.dto.pagination;

import com.javamentor.qa.platform.dao.abstracts.dto.pagination.PaginationDtoAble;
import com.javamentor.qa.platform.models.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@Repository("FindTextInGlobalChat")
@RequiredArgsConstructor
public class FindTextInGlobalChat implements PaginationDtoAble<MessageDto> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<MessageDto> getItems(Map<String, Object> param) {

        return   entityManager.createQuery("SELECT new com.javamentor.qa.platform.models.dto.MessageDto" +
                "(m.id, " +
                "m.message, " +
                "user.nickname, " +
                "user.id, " +
                "c.id," +
                "user.imageLink, " +
                "m.persistDate) " +
                "FROM Message m " +
                "JOIN Chat c ON c.id = m.chat.id " +
                "JOIN User user ON user.id = m.userSender.id " +
                "WHERE c.isGlobal = true " +
                "AND m.message LIKE  : textParam"
        , MessageDto.class).setParameter("textParam", "%" + param.get("text") + "%")
                .getResultList();
        }



    @Override
    public int getTotalResultCount(Map<String, Object> param) {
        return ((Long)entityManager.createQuery("SELECT COUNT(m) FROM Message m JOIN  m.chat WHERE m.chat.isGlobal = true AND m.message LIKE  : textParam")
                .setParameter("textParam", "%" + param.get("text") + "%").getSingleResult()).intValue();


    }
}
