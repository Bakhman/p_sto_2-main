package com.javamentor.qa.platform.api;

import com.javamentor.qa.platform.AbstractApiTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

/**
 * @author Bakhmai
 */
public class TestProfileUserResourceController extends AbstractApiTest {

    //проверка работы с пустой табл answer
    @Test
    @Sql(value = {"/script/ProfileUserResourceController/getTop10ByAnswerAndCountAnswerByUser/BeforeNullAnswer.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/script/ProfileUserResourceController/getTop10ByAnswerAndCountAnswerByUser/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetCountOfAnswersByUserToWeekNull() throws Exception {
        ResultActions res = mvc.perform(get("/api/user/101/profile/question/week")
                .header("Authorization", getJwtToken("vasya@mail.ru", "password")));
        res.andExpect(status().isOk());
        assertThat(Integer.valueOf(res.andReturn().getResponse().getContentAsString())).isEqualTo(0);
    }

    //проверка кол-во ответов
    @Test
    @Sql(value = {"/script/ProfileUserResourceController/getTop10ByAnswerAndCountAnswerByUser/Before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/script/ProfileUserResourceController/getTop10ByAnswerAndCountAnswerByUser/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetCountOfAnswersByUserToWeek() throws Exception {
        ResultActions res101 = mvc.perform(get("/api/user/101/profile/question/week")
                .header("Authorization", getJwtToken("vasya@mail.ru", "password")));
        res101.andExpect(status().isOk());
        assertThat(Integer.valueOf(res101.andReturn().getResponse().getContentAsString())).isEqualTo(4);
        ResultActions res102 = mvc.perform(get("/api/user/102/profile/question/week")
                .header("Authorization", getJwtToken("user2@mail.ru", "3111")));
        res102.andExpect(status().isOk());
        assertThat(Integer.valueOf(res102.andReturn().getResponse().getContentAsString())).isEqualTo(8);
    }

    @Test
    @Sql(value = {"/script/ProfileUserResourceController/getQuestionsByUser/Before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/script/ProfileUserResourceController/getQuestionsByUser/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getQuestionsByUserIdQuestionsExistTest() throws Exception {
        mvc.perform(get("/api/user/100/profile/questions")
                        .header("Authorization", getJwtToken("user100@mail.ru", "Password!1")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].questionId").value(100))
                .andExpect(jsonPath("$[0].title").value("Default_Answer1"))
                .andExpect(jsonPath("$[0].tagList").isEmpty())
                .andExpect(jsonPath("$[0].countAnswer").value("0"))
                .andExpect(jsonPath("$[0].persistDate").value("2022-10-25T21:40:00.725794"))
                .andExpect(jsonPath("$[1].questionId").value(101))
                .andExpect(jsonPath("$[1].title").value("Default_Answer2"))
                .andExpect(jsonPath("$[1].tagList").isEmpty())
                .andExpect(jsonPath("$[1].countAnswer").value("0"))
                .andExpect(jsonPath("$[1].persistDate").value("2022-10-25T21:40:00.725794"))
                .andExpect(jsonPath("$[2].questionId").value(102))
                .andExpect(jsonPath("$[2].title").value("Default_Answer3"))
                .andExpect(jsonPath("$[2].tagList").isEmpty())
                .andExpect(jsonPath("$[2].countAnswer").value("0"))
                .andExpect(jsonPath("$[2].persistDate").value("2022-10-25T21:40:00.725794"))
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    @Sql(value = {"/script/ProfileUserResourceController/getQuestionsByUser/Before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/script/ProfileUserResourceController/getQuestionsByUser/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getQuestionsByUserIdAbsentQuestionTest() throws Exception {
        mvc.perform(get("/api/user/101/profile/questions")
                        .header("Authorization", getJwtToken("user101@mail.ru", "Password!2")))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Для данного пользователя список вопросов отсутствует"));
    }

    @Test
    @Sql(value = {"/script/ProfileUserResourceController/getQuestionsByUser/Before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/script/ProfileUserResourceController/getQuestionsByUser/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getQuestionsByUserIdWrongIdTest() throws Exception {
        mvc.perform(get("/api/user/102/profile/questions")
                        .header("Authorization", getJwtToken("user101@mail.ru", "Password!2")))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Неверный ID пользователя"));
    }

    @Test
    @Sql(value = {"/script/ProfileUserResourceController/getBookmarksDtoByUserId/Before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/script/ProfileUserResourceController/getBookmarksDtoByUserId/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getBookmarksDtoByUserIdSuccessTest() throws Exception {
        mvc.perform(get("/api/user/100/profile/bookmarks")
                        .header("Authorization", getJwtToken("user100@mail.ru", "Password!1")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(100))
                .andExpect(jsonPath("$[0].questionId").value(100))
                .andExpect(jsonPath("$[0].persistDate").value("2022-10-25T21:40:00.725794"))
                .andExpect(jsonPath("$[1].id").value(102))
                .andExpect(jsonPath("$[1].questionId").value(101))
                .andExpect(jsonPath("$[1].persistDate").value("2022-10-25T21:40:00.725794"))
                .andExpect(jsonPath("$[2].id").value(103))
                .andExpect(jsonPath("$[2].questionId").value(102))
                .andExpect(jsonPath("$[2].persistDate").value("2022-10-25T21:40:00.725794"))
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    @Sql(value = {"/script/ProfileUserResourceController/getBookmarksDtoByUserId/Before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/script/ProfileUserResourceController/getBookmarksDtoByUserId/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getBookmarksDtoByUserIdWrongIdTest() throws Exception {
        mvc.perform(get("/api/user/102/profile/bookmarks")
                        .header("Authorization", getJwtToken("user100@mail.ru", "Password!1")))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Неверный ID пользователя"));
    }

    @Test
    @Sql(value = {"/script/ProfileUserResourceController/getBookmarksDtoByUserId/Before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/script/ProfileUserResourceController/getBookmarksDtoByUserId/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getBookmarksDtoByUserIdEmptyTest() throws Exception {
        mvc.perform(get("/api/user/101/profile/bookmarks")
                        .header("Authorization", getJwtToken("user100@mail.ru", "Password!1")))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Закладки не найдены"));
    }

    @Test
    @Sql(value = {"/script/ProfileUserResourceController/getDeletedQuestionsByUser/Before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/script/ProfileUserResourceController/getDeletedQuestionsByUser/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getDeletedQuestionsByUserSuccessTest() throws Exception {
        mvc.perform(get("/api/user/100/profile/delete/questions")
                        .header("Authorization", getJwtToken("user100@mail.ru", "Password!1")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].questionId").value(100))
                .andExpect(jsonPath("$[0].title").value("Default_Answer1"))
                .andExpect(jsonPath("$[0].tagList").isEmpty())
                .andExpect(jsonPath("$[0].countAnswer").value("0"))
                .andExpect(jsonPath("$[0].persistDate").value("2022-10-25T21:40:00.725794"))
                .andExpect(jsonPath("$[1].questionId").value(102))
                .andExpect(jsonPath("$[1].title").value("Default_Answer3"))
                .andExpect(jsonPath("$[1].tagList").isEmpty())
                .andExpect(jsonPath("$[1].countAnswer").value("0"))
                .andExpect(jsonPath("$[1].persistDate").value("2022-10-25T21:40:00.725794"))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @Sql(value = {"/script/ProfileUserResourceController/getDeletedQuestionsByUser/Before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/script/ProfileUserResourceController/getDeletedQuestionsByUser/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getDeletedQuestionsByUserEmptyListTest() throws Exception {
        mvc.perform(get("/api/user/101/profile/delete/questions")
                        .header("Authorization", getJwtToken("user100@mail.ru", "Password!1")))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Для данного пользователя список удаленных вопросов отсутствует"));
    }

    @Test
    @Sql(value = {"/script/ProfileUserResourceController/getDeletedQuestionsByUser/Before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/script/ProfileUserResourceController/getDeletedQuestionsByUser/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getDeletedQuestionsByUserWrongIdTest() throws Exception {
        mvc.perform(get("/api/user/102/profile/delete/questions")
                        .header("Authorization", getJwtToken("user100@mail.ru", "Password!1")))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Неверный ID пользователя"));
    }
}
