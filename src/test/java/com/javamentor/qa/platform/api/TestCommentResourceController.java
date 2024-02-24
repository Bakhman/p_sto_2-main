package com.javamentor.qa.platform.api;

import com.javamentor.qa.platform.AbstractApiTest;
import com.javamentor.qa.platform.models.entity.Comment;
import com.javamentor.qa.platform.models.entity.CommentType;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestCommentResourceController extends AbstractApiTest {

    @Test
    @Sql(value = {"/script/TestCommentResourceController/GetAllCommentDtoInPages/After.sql"}, executionPhase =
            Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"/script/TestCommentResourceController/GetAllCommentDtoInPages/Before.sql"}, executionPhase =
            Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void checkFirstPageWithTwoItemsOnPage() throws Exception {
        String access_token = getJwtToken("user100@mail.ru", "12345");
        mvc.perform(get("/api/user/comment/question/{questionId}", 100)
                .header("Authorization", access_token)
                .param("currentPageNumber", "1")
                .param("itemsOnPage", "2"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("currentPageNumber")
                        .value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("itemsOnPage")
                        .value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("totalPageCount")
                        .value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("totalResultCount")
                        .value(7))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id")
                        .value(106))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].comment")
                        .value("It is wonderful!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[1].id")
                        .value(105))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[1].comment")
                        .value("It is great!"));

    }

    @Test
    @Sql(value = {"/script/TestCommentResourceController/GetAllCommentDtoInPages/After.sql"}, executionPhase =
            Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"/script/TestCommentResourceController/GetAllCommentDtoInPages/Before.sql"}, executionPhase =
            Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void checkPageSevenWithOneItemAllFields() throws Exception {
        String access_token = getJwtToken("user100@mail.ru", "12345");
        mvc.perform(get("/api/user/comment/question/{questionId}", 100)
                .header("Authorization", access_token)
                .param("currentPageNumber", "7")
                .param("itemsOnPage", "1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("totalPageCount")
                        .value(7))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id")
                        .value(100))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].comment")
                        .value("It is cool!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].userId")
                        .value(101))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].fullName")
                        .value("user101"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].reputation")
                        .value(500))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].dateAdded")
                        .value("2022-08-12T17:05:01.197649"));

    }

    @Test
    @Sql(value = {"/script/TestCommentResourceController/GetAllCommentDtoInPages/After.sql"}, executionPhase =
            Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"/script/TestCommentResourceController/GetAllCommentDtoInPages/Before.sql"}, executionPhase =
            Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void catchQuestionIdNotFound() throws Exception {
        String access_token = getJwtToken("user100@mail.ru", "12345");
        mvc.perform(get("/api/user/comment/question/{questionId}", 99)
                .header("Authorization", access_token)
                .param("currentPageNumber", "1")
                .param("itemsOnPage", "2"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Sql(value = {"/script/TestCommentResourceController/GetAllAnswerCommentDtoInPage/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"/script/TestCommentResourceController/GetAllAnswerCommentDtoInPage/Before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void checkAnswerToCommentPageNumberOne() throws Exception {
        final String access_token = getJwtToken("user100@mail.ru", "$2a$12$RbzrY3EwB4O/UElqKDY..e8e8t88SijJ0iVtMBE0VYsQUxV6Wtvhu");

        mvc.perform(get("/api/user/comment/answer/{answerId}", 100)
                        .header("Authorization", access_token)
                        .param("itemsOnPage", "3")
                        .param("currentPageNumber", "1")
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("currentPageNumber").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("itemsOnPage").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("totalResultCount").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("totalPageCount").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[2].comment").value("MessageThree!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[2].dateAdded").value("2022-08-12T17:03:24.197649"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[2].id").value(102))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[2].userId").value(101))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[2].reputation").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[2].fullName").value("user101"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[1].comment").value("MessageFor!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[1].dateAdded").value("2022-08-12T17:04:24.197649"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[1].id").value(103))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[1].userId").value(102))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[1].reputation").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[1].fullName").value("user102"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].comment").value("MessageFive!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].dateAdded").value("2022-08-12T17:05:24.197649"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id").value(104))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].userId").value(102))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].reputation").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].fullName").value("user102"));

    }
    @Test
    @Sql(value = {"/script/TestCommentResourceController/GetAllAnswerCommentDtoInPage/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"/script/TestCommentResourceController/GetAllAnswerCommentDtoInPage/Before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void checkOnePageItemField() throws Exception {
        final String access_token = getJwtToken("user100@mail.ru", "$2a$12$RbzrY3EwB4O/UElqKDY..e8e8t88SijJ0iVtMBE0VYsQUxV6Wtvhu");

        mvc.perform(get("/api/user/comment/answer/{answerId}", 100)
                        .header("Authorization", access_token)
                        .param("itemsOnPage", "4")
                        .param("currentPageNumber", "2"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].userId").value(101))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].fullName").value("user101"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].reputation").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].dateAdded").value("2022-08-12T17:01:24.197649"));

    }
    @Test
    @Sql(value = {"/script/TestCommentResourceController/GetAllAnswerCommentDtoInPage/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"/script/TestCommentResourceController/GetAllAnswerCommentDtoInPage/Before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void checkCommentByNotExistAnswerId() throws Exception {
        final String access_token = getJwtToken("user100@mail.ru", "$2a$12$RbzrY3EwB4O/UElqKDY..e8e8t88SijJ0iVtMBE0VYsQUxV6Wtvhu");

        mvc.perform(get("/api/user/comment/answer/{answerId}", 999)
                        .header("Authorization", access_token)
                        .param("itemsOnPage", "3")
                        .param("currentPageNumber", "1")
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(value = {"/script/TestCommentResourceController/GetAllAnswerCommentDtoInPage/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"/script/TestCommentResourceController/GetAllAnswerCommentDtoInPage/Before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void checkCommentByIsDeletedAnswerId() throws Exception {
        final String access_token = getJwtToken("user100@mail.ru", "$2a$12$RbzrY3EwB4O/UElqKDY..e8e8t88SijJ0iVtMBE0VYsQUxV6Wtvhu");

        mvc.perform(get("/api/user/comment/answer/{answerId}", 300)
                        .header("Authorization", access_token)
                        .param("itemsOnPage", "3")
                        .param("currentPageNumber", "1")
                )
                .andExpect(status().isBadRequest());
    }
    @Test
    @Sql(value = {"/script/TestCommentResourceController/checkAddAnswerToComment/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"/script/TestCommentResourceController/checkAddAnswerToComment/Before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void checkAddCommentToAnswerTest() throws Exception {
        final String access_token = getJwtToken("user100@mail.ru", "$2a$12$RbzrY3EwB4O/UElqKDY..e8e8t88SijJ0iVtMBE0VYsQUxV6Wtvhu");
        final String comment = "My first Api";

        mvc.perform(post("/api/user/comment/answer/{answerId}", 100)
                        .header("Authorization", access_token)
                        .contentType(MediaType.TEXT_EVENT_STREAM)
                        .content(comment)
                        .characterEncoding("UTF-8"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(status().isOk());

        Comment saveComment = em.find(Comment.class, 1L);
        Assert.assertEquals("Тест не пройден, так как коментарий не был добавлен к ответу",comment,saveComment.getText());
        Assert.assertEquals("Тест не пройден, к коментарию был добавлен не верный отправитель.",100L,saveComment.getUser().getId().longValue());
        Assert.assertEquals("Тест не пройден, к комментарию был добавлен не к ответу", CommentType.ANSWER,saveComment.getCommentType());
        Assert.assertEquals("Тест не пройден, назначенное время создания отличается от последнего обновления.",
                saveComment.getPersistDateTime(),saveComment.getLastUpdateDateTime());
    }
    @Test
    @Sql(value = {"/script/TestCommentResourceController/checkAddAnswerToComment/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"/script/TestCommentResourceController/checkAddAnswerToComment/Before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void checkAddCommentThatWasDeletedTest() throws Exception {
        final String access_token = getJwtToken("user100@mail.ru", "$2a$12$RbzrY3EwB4O/UElqKDY..e8e8t88SijJ0iVtMBE0VYsQUxV6Wtvhu");
        final String comment = "MyFirstApi where comment is deleted";
        final String text = objectMapper.writeValueAsString(comment);
        mvc.perform(post("/api/user/comment/answer/{answerId}", 200)
                        .header("Authorization", access_token)
                        .contentType(MediaType.TEXT_EVENT_STREAM)
                        .content(text)
                        .content(comment)
                        .characterEncoding("UTF-8"))
                .andExpect(status().is4xxClientError());

        mvc.perform(post("/api/user/comment/answer/{answerId}", 300)
                        .header("Authorization", access_token)
                        .contentType(MediaType.TEXT_EVENT_STREAM)
                        .content(text)
                        .content(comment)
                        .characterEncoding("UTF-8"))
                .andExpect(status().is4xxClientError());
    }
    @Test
    @Sql(value = {"/script/TestCommentResourceController/checkAddAnswerToComment/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"/script/TestCommentResourceController/checkAddAnswerToComment/Before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void checkAddCommentWithEmptyTextTest() throws Exception {
        final String access_token = getJwtToken("user100@mail.ru", "$2a$12$RbzrY3EwB4O/UElqKDY..e8e8t88SijJ0iVtMBE0VYsQUxV6Wtvhu");
        final String comment = " ";
        mvc.perform(post("/api/user/comment/answer/{answerId}", 100)
                .header("Authorization", access_token)
                .contentType(MediaType.TEXT_EVENT_STREAM)
                        .content(comment)
                        .characterEncoding("UTF-8"))
                .andExpect(status().is4xxClientError());
    }
    @Test
    @Sql(value = {"/script/TestCommentResourceController/checkAddAnswerToComment/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"/script/TestCommentResourceController/checkAddAnswerToComment/Before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void checkAddCommentThatDoesntExistTest() throws Exception {
        final String access_token = getJwtToken("user100@mail.ru", "$2a$12$RbzrY3EwB4O/UElqKDY..e8e8t88SijJ0iVtMBE0VYsQUxV6Wtvhu");

        final String comment = "Ответа не существует";
        mvc.perform(post("/api/user/comment/answer/{answerId}", 400)
                .header("Authorization", access_token)
                .contentType(MediaType.TEXT_EVENT_STREAM)
                        .content(comment)
                        .characterEncoding("UTF-8"))
                .andExpect(status().is4xxClientError());
    }
}
