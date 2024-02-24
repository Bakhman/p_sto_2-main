package com.javamentor.qa.platform.api;

import com.javamentor.qa.platform.AbstractApiTest;
import com.javamentor.qa.platform.models.entity.chat.GroupChat;
import com.javamentor.qa.platform.models.entity.user.User;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestChatResourceController extends AbstractApiTest {

    private EntityManager entityManager;
    @Autowired
    public TestChatResourceController(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Test
    @Sql(value = {"/script/ChatResourceController/joinUserByUserChat/Before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/script/ChatResourceController/joinUserByUserChat/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void joinUserByUserChat() throws Exception {
        String accessToken = getJwtToken("vasya1@mail.ru", "password1");

        mvc.perform(post("/api/user/chat/group/{id}/join", 100)
                        .header("Authorization", accessToken)
                        .content(objectMapper.writeValueAsString(100L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        long countUserInGroupChat = em.createQuery("select count(gc) from GroupChat gc join gc.users as user" +
                " where gc.id = 100 and user.id = 100",Long.class).getSingleResult();
        Assert.assertEquals("Пользователь не добавлен в групповой чат", countUserInGroupChat, 1L);
    }

    @Test
    @Sql(value = {"/script/ChatResourceController/joinUserByUserChat/Before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/script/ChatResourceController/joinUserByUserChat/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void joinNotExistUserByUserChat() throws Exception {
        String accessToken = getJwtToken("vasya2@mail.ru", "password2");

        mvc.perform(post("/api/user/chat/group/{id}/join", 100)
                        .header("Authorization", accessToken)
                        .content(objectMapper.writeValueAsString(999L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

        long countUserInGroupChat = em.createQuery("select count(gc) from GroupChat gc join gc.users as user" +
                " where gc.id = 100 and user.id = 999",Long.class).getSingleResult();
        Assert.assertEquals("Тест не пройден", countUserInGroupChat, 0L);
    }

    @Test
    @Sql(value = {"/script/ChatResourceController/joinUserByUserChat/Before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/script/ChatResourceController/joinUserByUserChat/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void jointUserByNotExisUserChat() throws Exception {
        String accessToken = getJwtToken("vasya3@mail.ru", "password3");

        mvc.perform(post("/api/user/chat/group/{id}/join", 999)
                        .header("Authorization", accessToken)
                        .content(objectMapper.writeValueAsString(100L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

        long countUserInGroupChat = em.createQuery("select count(gc) from GroupChat gc join gc.users as user" +
                " where gc.id = 999 and user.id = 100",Long.class).getSingleResult();
        Assert.assertEquals("Тест не пройден", countUserInGroupChat, 0L);
    }

    @Test
    @Sql(value = {"/script/ChatResourceController/joinUserByUserChat/Before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/script/ChatResourceController/joinUserByUserChat/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void jointAlreadyAddedUserToUserChat() throws Exception {
        String accessToken = getJwtToken("vasya4@mail.ru", "password4");

        mvc.perform(post("/api/user/chat/group/{id}/join", 100)
                        .header("Authorization", accessToken)
                        .content(objectMapper.writeValueAsString(103L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

        long countUserInGroupChat = em.createQuery("select count(gc) from GroupChat gc join gc.users as user" +
                " where gc.id = 100 and user.id = 103",Long.class).getSingleResult();
        Assert.assertEquals("Тест не пройден", countUserInGroupChat, 1L);
    }

    @Test
    @Sql(value = {"/script/ChatResourceController/updateImageGroupChat/Before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/script/ChatResourceController/updateImageGroupChat/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateImageGroupChatTest() throws Exception {

        String groupChatImageQuery = "SELECT gr FROM GroupChat gr WHERE gr.id = :id";
        TypedQuery<GroupChat> query = entityManager.createQuery(groupChatImageQuery, GroupChat.class)
                .setParameter("id", 101L);
        String oldImage = query.getSingleResult().getImageLink();

        String updatedImage = "newImageLinkOfGroupChat";

        mvc.perform(patch("/api/user/chat/{id}/group/image", 101)
                        .header("Authorization", getJwtToken("100_user@mail.ru", "123"))
                        .contentType("text/plain;charset=UTF-8")
                        .content(updatedImage)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string("Image successfully update"));

        query = entityManager.createQuery(groupChatImageQuery, GroupChat.class).setParameter("id", 101L);
        Assert.assertNotEquals(oldImage, query.getSingleResult().getImageLink());
        query = entityManager.createQuery(groupChatImageQuery, GroupChat.class).setParameter("id", 101L);
        Assert.assertEquals(updatedImage, query.getSingleResult().getImageLink());

        mvc.perform(patch("/api/user/chat/{id}/group/image", 100)
                        .header("Authorization", getJwtToken("100_user@mail.ru", "123"))
                        .contentType("text/plain;charset=UTF-8")
                        .content("/newImageGroupChat")
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string("Group chat is not exist"));

    }
    @Test
    @Sql(value = {"/script/ChatResourceController/deleteUserFromGroupChat/Before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/script/ChatResourceController/deleteUserFromGroupChat/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteUserFromGroupChat() throws Exception {

        String groupChatWithUsers = "SELECT gr FROM GroupChat gr LEFT JOIN FETCH gr.users WHERE gr.id = :id";

        //��� �� ���������

        mvc.perform(delete("/api/user/chat/{groupId}/group", 100)
                        .header("Authorization", getJwtToken("100_user@mail.ru", "123"))
                        .param("userId", "101")
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string("Group chat is not exist"));

        //���������� �������� ������� ���� ������������

        TypedQuery<GroupChat> query = entityManager.createQuery(groupChatWithUsers, GroupChat.class)
                .setParameter("id", 101L);
        Set<User> users = query.getSingleResult().getUsers();
        Assert.assertTrue(users.stream().anyMatch(user -> user.getId().equals(101L)));
        mvc.perform(delete("/api/user/chat/{groupId}/group", 101)
                        .header("Authorization", getJwtToken("100_user@mail.ru", "123"))
                        .param("userId", "101")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string("User successfully delete from group chat"));
        query = entityManager.createQuery(groupChatWithUsers, GroupChat.class).setParameter("id", 101L);
        Assert.assertTrue(!query.getSingleResult().getUsers().stream().anyMatch(user -> user.getId().equals(101L)));

        //�������� ������� �� �����

        mvc.perform(delete("/api/user/chat/{groupId}/group", 101)
                        .header("Authorization", getJwtToken("102_user@mail.ru", "123"))
                        .param("userId", "100")
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string("Only group chat Author can delete user"));

        //�������� ������� ������������ �������� ��� � ����

        mvc.perform(delete("/api/user/chat/{groupId}/group", 101)
                        .header("Authorization", getJwtToken("100_user@mail.ru", "123"))
                        .param("userId", "105")
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string("Group Chat 101 not include this user"));

        //�������������� ������������ ������� �� ���� ��� ����

        query = entityManager.createQuery(groupChatWithUsers, GroupChat.class).setParameter("id", 102L);
        users = query.getSingleResult().getUsers();
        Assert.assertTrue(users.stream().anyMatch(user -> user.getId().equals(101L)));
        mvc.perform(delete("/api/user/chat/{groupId}/group", 102)
                        .header("Authorization", getJwtToken("101_user@mail.ru", "123"))
                        .param("userId", "101")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string("User successfully delete from group chat"));
        query = entityManager.createQuery(groupChatWithUsers, GroupChat.class).setParameter("id", 102L);
        Assert.assertTrue(!query.getSingleResult().getUsers().stream().anyMatch(user -> user.getId().equals(101L)));


        //�������� ������� ��� �������� id ������������

        mvc.perform(delete("/api/user/chat/{groupId}/group", 101)
                        .header("Authorization", getJwtToken("100_user@mail.ru", "123"))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Required Long parameter 'userId' is not present"));
    }

    @Test
    @Sql(value = {"/script/ChatResourceController/GetPaginationMessagesSortedDate/After.sql"}, executionPhase =
            Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"/script/ChatResourceController/GetPaginationMessagesSortedDate/Before.sql"}, executionPhase =
            Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void getAllMessagesSortedByDate() throws Exception {
        String access_token = getJwtToken ("user100@mail.ru","12345");
        mvc.perform(get("/api/user/chat/{id}/single/message", 100)
                        .header("Authorization", access_token)
                        .param("currentPageNumber", "1")
                        .param("text", ""))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(102))
                .andExpect(jsonPath("$[0].message").value("some message 1020"))
                .andExpect(jsonPath("$[0].nickName").value("user101"))
                .andExpect(jsonPath("$[0].userId").value(101))
                .andExpect(jsonPath("$[0].chatId").value(100))
                .andExpect(jsonPath("$[0].image").value("null"))
                .andExpect(jsonPath("$[0].persistDateTime").value("2022-08-12T17:05:24.197657"))

                .andExpect(jsonPath("$[1].id").value(101))
                .andExpect(jsonPath("$[1].message").value("some message 1012"))
                .andExpect(jsonPath("$[1].nickName").value("user100"))
                .andExpect(jsonPath("$[1].userId").value(100))
                .andExpect(jsonPath("$[1].chatId").value(100))
                .andExpect(jsonPath("$[1].image").value("null"))
                .andExpect(jsonPath("$[1].persistDateTime").value("2022-08-12T17:05:24.197656"))

                .andExpect(jsonPath("$[2].id").value(100))
                .andExpect(jsonPath("$[2].message").value("some message 1010"))
                .andExpect(jsonPath("$[2].nickName").value("user100"))
                .andExpect(jsonPath("$[2].userId").value(100))
                .andExpect(jsonPath("$[2].chatId").value(100))
                .andExpect(jsonPath("$[2].image").value("null"))
                .andExpect(jsonPath("$[2].persistDateTime").value("2022-08-12T17:05:24.197655"));
    }


    @Test
    @Sql(value = {"/script/ChatResourceController/GetPaginationMessagesSortedDate/After.sql"}, executionPhase =
            Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"/script/ChatResourceController/GetPaginationMessagesSortedDate/Before.sql"}, executionPhase =
            Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void getMessagesSortedByDateWithSome() throws Exception {
        String access_token = getJwtToken ("user100@mail.ru","12345");
        mvc.perform(get("/api/user/chat/{id}/single/message", 100)
                        .header("Authorization", access_token)
                        .param("currentPageNumber", "1")
                        .param("text", "101"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$[0].id").value(101))
                .andExpect(jsonPath("$[0].message").value("some message 1012"))
                .andExpect(jsonPath("$[0].nickName").value("user100"))
                .andExpect(jsonPath("$[0].userId").value(100))
                .andExpect(jsonPath("$[0].chatId").value(100))
                .andExpect(jsonPath("$[0].image").value("null"))
                .andExpect(jsonPath("$[0].persistDateTime").value("2022-08-12T17:05:24.197656"))

                .andExpect(jsonPath("$[1].id").value(100))
                .andExpect(jsonPath("$[1].message").value("some message 1010"))
                .andExpect(jsonPath("$[1].nickName").value("user100"))
                .andExpect(jsonPath("$[1].userId").value(100))
                .andExpect(jsonPath("$[1].chatId").value(100))
                .andExpect(jsonPath("$[1].image").value("null"))
                .andExpect(jsonPath("$[1].persistDateTime").value("2022-08-12T17:05:24.197655"))

                .andExpect(jsonPath("$[2].id").doesNotExist());
    }

    @Test
    @Sql(value = {"/script/ChatResourceController/GetPaginationMessagesSortedDate/After.sql"}, executionPhase =
            Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"/script/ChatResourceController/GetPaginationMessagesSortedDate/Before.sql"}, executionPhase =
            Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void getMessagesSortedByDateWith100() throws Exception {
        String access_token = getJwtToken ("user100@mail.ru","12345");
        mvc.perform(get("/api/user/chat/{id}/single/message", 100)
                        .header("Authorization", access_token)
                        .param("currentPageNumber", "1")
                        .param("text", "1010"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(100))
                .andExpect(jsonPath("$[0].message").value("some message 1010"))
                .andExpect(jsonPath("$[0].nickName").value("user100"))
                .andExpect(jsonPath("$[0].userId").value(100))
                .andExpect(jsonPath("$[0].chatId").value(100))
                .andExpect(jsonPath("$[0].image").value("null"))
                .andExpect(jsonPath("$[0].persistDateTime").value("2022-08-12T17:05:24.197655"))

                .andExpect(jsonPath("$[1].id").doesNotExist());
    }

    @Test
    @Sql(value = "/script/ChatResourceController/checkGetMessageByGroupChatId/Before.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/script/ChatResourceController/checkGetMessageByGroupChatId/After.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void checkGetMessageByGroupIdChat() throws Exception {
        String access_token = getJwtToken("user100@mail.ru", "12345");

        mvc.perform(get("/api/user/chat/{id}/group/message",101)
                        .param("itemsOnPage","2")
                        .param("currentPageNumber","1")
                        .header("Authorization",access_token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(53))
                .andExpect(jsonPath("$[0].message").value("MyMessageThree53"))
                .andExpect(jsonPath("$[0].userId").value(101))
                .andExpect(jsonPath("$[0].chatId").value(101))
                .andExpect(jsonPath("$[0].persistDateTime").value("2022-08-12T17:06:24.197649"))
                .andExpect(jsonPath("$[0].nickName").value("user101"))
                .andExpect(jsonPath("$[0].image").value(""))
                .andExpect(jsonPath("$[1].id").value(52))
                .andExpect(jsonPath("$[1].message").value("MyMessageTwo52"))
                .andExpect(jsonPath("$[1].userId").value(102))
                .andExpect(jsonPath("$[1].chatId").value(101))
                .andExpect(jsonPath("$[1].persistDateTime").value("2022-08-12T17:05:24.197649"))
                .andExpect(jsonPath("$[1].nickName").value("user102"))
                .andExpect(jsonPath("$[1].image").value(""));

    }
    @Test
    @Sql(value = "/script/ChatResourceController/checkGetMessageByGroupChatId/Before.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/script/ChatResourceController/checkGetMessageByGroupChatId/After.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void checkGetMessagesFromAnotherGroupChat() throws Exception {
        String access_token = getJwtToken("user100@mail.ru", "12345");

        mvc.perform(get("/api/user/chat/{id}/group/message",102)
                        .param("itemsOnPage","2")
                        .param("currentPageNumber","1")
                        .header("Authorization",access_token))
                .andExpect(status().isNotFound());
    }
    @Test
    @Sql(value = "/script/ChatResourceController/checkGetMessageByGroupChatId/Before.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/script/ChatResourceController/checkGetMessageByGroupChatId/After.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void checkGetMessageFromGroupChatIsExist() throws Exception {
        String access_token = getJwtToken("user100@mail.ru", "12345");

        mvc.perform(get("/api/user/chat/{id}/group/message",999)
                        .param("itemsOnPage","2")
                        .param("currentPageNumber","1")
                        .header("Authorization",access_token))
                .andExpect(status().isNotFound());
    }



    @Test
    @Sql(value = {"/script/ChatResourceController/deleteUserFromSingleChat/Before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/script/ChatResourceController/deleteUserFromSingleChat/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteUserFromSingleChatSuccessTest() throws Exception {
        String access_token = getJwtToken ("user100@mail.ru","Password!1");

        mvc.perform(delete("/api/user/chat/{id}/single", 100)
                        .header("Authorization", access_token))
                .andExpect(status().isOk())
                .andExpect(content().string("Пользователь успешно удалён из чата"));
    }

    @Test
    @Sql(value = {"/script/ChatResourceController/deleteUserFromSingleChat/Before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/script/ChatResourceController/deleteUserFromSingleChat/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteUserFromSingleChatEmptyChatTest() throws Exception {
        String access_token = getJwtToken ("user100@mail.ru","Password!1");

        mvc.perform(delete("/api/user/chat/{id}/single", 103)
                        .header("Authorization", access_token))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Single Chat с данным ID не найден."));
    }

    @Test
    @Sql(value = {"/script/ChatResourceController/deleteUserFromSingleChat/Before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/script/ChatResourceController/deleteUserFromSingleChat/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteUserFromSingleChatErrorTest() throws Exception {
        String access_token = getJwtToken ("user100@mail.ru","Password!1");

        mvc.perform(delete("/api/user/chat/{id}/single", 102)
                        .header("Authorization", access_token))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Single Chat № 102 для данного пользователя уже удален."));
    }
}
