package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.ChatDtoDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.dto.ChatDto;
import com.javamentor.qa.platform.models.dto.GroupChatDto;
import com.javamentor.qa.platform.models.dto.MessageDto;
import com.javamentor.qa.platform.models.dto.SingleChatDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ChatDtoDaoImpl implements ChatDtoDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<SingleChatDto> getAllSingleChatDto(Long userId) {
        return entityManager.createQuery(
                        "SELECT new com.javamentor.qa.platform.models.dto.SingleChatDto" +
                                "(c.id, " +
                                "CASE WHEN c.userOne.id = u.id THEN c.useTwo.fullName WHEN c.useTwo.id = u.id THEN c.userOne.fullName END, " +
                                "CASE WHEN c.userOne.id = u.id THEN c.useTwo.imageLink WHEN c.useTwo.id = u.id THEN  c.userOne.imageLink END, " +
                                "m.message, " +
                                "m.persistDate) " +
                                "FROM SingleChat c " +
                                "LEFT JOIN User u ON u.id = c.userOne.id " +
                                "LEFT JOIN Message m ON c.chat.id = m.chat.id " +
                                "AND m.persistDate = (select max(persistDate) from m WHERE m.chat.id = c.chat.id) " +
                                "WHERE c.userOne.id = :userId ", SingleChatDto.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<ChatDto> getSingleChatsByString(String string, Long userId) {
        List<ChatDto> listOfSingleChat = entityManager.createQuery(
                        "SELECT new com.javamentor.qa.platform.models.dto.ChatDto " +
                                "(c.id, " +
                                "CASE WHEN c.chatType = 0 AND s.userOne.id = u.id THEN s.useTwo.nickname " +
                                "WHEN c.chatType = 0 AND s.useTwo.id = u.id THEN s.userOne.nickname END  , " +
                                "CASE WHEN c.chatType = 0 AND s.userOne.id = u.id THEN s.useTwo.imageLink " +
                                "WHEN c.chatType = 0 AND s.useTwo = u.id THEN s.userOne.imageLink END , " +
                                "m.message, " +
                                "m.persistDate," +
                                "c.isChatPin) " +
                                "FROM Chat c " +
                                "LEFT JOIN UserChatPin ucp ON ucp.chat.id = c.id " +
                                "AND c.isChatPin = true " +
                                "AND ucp.user.id = :userId " +
                                "LEFT JOIN Message m ON m.chat.id = c.id AND " +
                                "m.persistDate = (select max(persistDate) from m WHERE m.chat.id = c.id) " +
                                "LEFT JOIN SingleChat s ON s.chat.id = c.id " +
                                "LEFT JOIN User u ON u.id = :userId " +
                                "WHERE (s.userOne.id = :userId OR s.useTwo.id = :userId) AND " +
                                "(LOWER(s.userOne.nickname) LIKE LOWER(CONCAT('%', :string, '%')) OR " +
                                "LOWER(s.useTwo.nickname) LIKE LOWER(CONCAT('%', :string, '%')))" +
                                "ORDER BY c.isChatPin DESC, m.persistDate DESC",
                        ChatDto.class)
                .setParameter("userId", userId)
                .setParameter("string", string)
                .getResultList();
        return listOfSingleChat;
    }

        @Override
        public List<ChatDto> getGroupChatsByString(String string, Long userId) {
        List<ChatDto> listOfGroupChat = entityManager.createQuery(
                        "SELECT new com.javamentor.qa.platform.models.dto.ChatDto " +
                                "(c.id, " +
                                "c.title , " +
                                "g.imageLink , " +
                                "m.message, " +
                                "m.persistDate," +
                                "c.isChatPin) " +
                                "FROM Chat c " +
                                "LEFT JOIN UserChatPin ucp ON ucp.chat.id = c.id " +
                                "AND c.isChatPin = true " +
                                "AND ucp.user.id = :userId " +
                                "LEFT JOIN Message m ON m.chat.id = c.id AND " +
                                "m.persistDate = (select max(persistDate) from m WHERE m.chat.id = c.id)" +
                                "LEFT JOIN GroupChat g ON g.chat.id = c.id " +
                                "LEFT JOIN g.users ul " +
                                "LEFT JOIN User u ON u.id = :userId " +
                                "WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :string, '%')) AND ul.id = :userId " +
                                "ORDER BY c.isChatPin DESC, m.persistDate DESC",
                        ChatDto.class)
                .setParameter("userId", userId)
                .setParameter("string", string)
                .getResultList();
        return listOfGroupChat;

    }

    @Override
    public Optional<GroupChatDto> getGroupChatByChatId(Long chatId) {
        Optional<GroupChatDto> o = SingleResultUtil.getSingleResultOrNull(entityManager.createQuery(
                        "SELECT new com.javamentor.qa.platform.models.dto.GroupChatDto" +
                                "(c.id, " +
                                "c.title, " +
                                "c.persistDate," +
                                "c.isChatPin) " +
                                "FROM Chat c " +
                                "WHERE c.id = :chatId ", GroupChatDto.class)
                .setParameter("chatId", chatId));
        return o;
    }

    public List<MessageDto> getMessageFromGlobalChat() {
        return entityManager.createQuery(
                        "SELECT new com.javamentor.qa.platform.models.dto.MessageDto" +
                                "(m.id, " +
                                "m.message, " +
                                "u.fullName, " +
                                "u.id, " +
                                "c.id," +
                                "u.imageLink, " +
                                "u.persistDateTime) " +
                                "FROM Message m " +
                                "JOIN Chat c ON c.id = m.chat.id " +
                                "JOIN User u ON u.id = m.userSender.id " +
                                "WHERE c.isGlobal = true ")
                .getResultList();
    }
}
