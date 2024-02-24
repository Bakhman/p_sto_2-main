package com.javamentor.qa.platform;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class TestQuestionResourceController extends AbstractApiTest {

    @Test
    @Sql(value = {"/script/QuestionResourceController/BookmarkQuestion/After.sql"}, executionPhase =
            Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"/script/QuestionResourceController/BookmarkQuestion/Before.sql"}, executionPhase =
            Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void addBookmarkAlreadyExist() throws Exception {
        String access_token = getJwtToken ("user100@mail.ru","12345");
        mvc.perform(post("/api/user/question/{questionId}/bookmark", 100)
                        .header("Authorization", access_token))
                .andExpect(status().isOk())
                .andExpect(content().string("Вопрос с id: 100 уже добавлен в закладки"));
    }
    @Test
    @Sql(value = {"/script/QuestionResourceController/BookmarkQuestion/After.sql"}, executionPhase =
            Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"/script/QuestionResourceController/BookmarkQuestion/Before.sql"}, executionPhase =
            Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void addBookmarkSuccess() throws Exception {
        String access_token = getJwtToken ("user100@mail.ru","12345");
        mvc.perform(post("/api/user/question/{questionId}/bookmark", 101)
                        .header("Authorization", access_token))
                .andExpect(status().isOk())
                .andExpect(content().string("Вопрос с id: 101 успешно добавлен в закладки"));
    }

    @Test
    @Sql(value = {"/script/QuestionResourceController/BookmarkQuestion/After.sql"}, executionPhase =
            Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"/script/QuestionResourceController/BookmarkQuestion/Before.sql"}, executionPhase =
            Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void addBookmarkNotFoundQuestion() throws Exception {
        String access_token = getJwtToken ("user100@mail.ru","12345");
        mvc.perform(post("/api/user/question/{questionId}/bookmark", 110)
                        .header("Authorization", access_token))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("Вопрос с id: 110 не существует"));
    }
}