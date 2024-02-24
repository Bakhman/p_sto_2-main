package com.javamentor.qa.platform.api;
import org.junit.Assert;
import com.javamentor.qa.platform.AbstractApiTest;
import com.javamentor.qa.platform.models.dto.QuestionCreateDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.List;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestQuestionResourceController extends AbstractApiTest {
    @Test
    @Sql(value = {"/script/TestQuestionResourceController/getQuestionDtoById/After.sql"}, executionPhase =
            Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"/script/TestQuestionResourceController/getQuestionDtoById/Before.sql"}, executionPhase =
            Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void getQuestionDtoByIdAndCheckFields() throws Exception {
        String access_token = getJwtToken("user100@mail.ru", "12345");
        mvc.perform(get("/api/user/question/{id}", 100)
                        .header("Authorization", access_token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id")
                        .value(100))
                .andExpect(jsonPath("$.title")
                        .value("title0"))
                .andExpect(jsonPath("$.authorId")
                        .value("100"))
                .andExpect(jsonPath("$.authorReputation")
                        .value(600))
                .andExpect(jsonPath("$.authorName")
                        .value("user100"))
                .andExpect(jsonPath("$.description")
                        .value("description0"))
                .andExpect(jsonPath("$.persistDateTime")
                        .value("2022-10-24T17:46:51.936176"))
                .andExpect(jsonPath("$.lastUpdateDateTime")
                        .value("2022-10-26T17:46:51.936176"));
    }

    @Test
    @Sql(value = {"/script/TestQuestionResourceController/tryToCreateQuestion/After.sql"}, executionPhase =
            Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"/script/TestQuestionResourceController/tryToCreateQuestion/Before.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void tryToCreateQuestion() throws Exception {
        String access_token = getJwtToken("user100@mail.ru", "12345");
        mvc.perform(post("/api/user/question")
                .header("Authorization", access_token)
                .content(objectMapper.writeValueAsString
                        (new QuestionCreateDto
                                ("question1", "question1Description",
                                        List.of("tag1", "tag2", "tag3"))))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
        Question question = em.find(Question.class, 1L);
        Assert.assertEquals("Не тот пользователь задает вопрос", 100L,
                question.getUser().getId().longValue());
        Assert.assertEquals("Не тот title", "question1", question.getTitle());
        Assert.assertEquals("Не тот description", "question1Description", question.getDescription());
    }

    @Test
    @Sql(value = {"/script/TestQuestionResourceController/tryToCreateQuestion/After.sql"}, executionPhase =
            Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"/script/TestQuestionResourceController/tryToCreateQuestion/Before.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void tryToCreateQuestionWithEmptyOrNullDtoFields() throws Exception {
        String access_token = getJwtToken("user100@mail.ru", "12345");
        mvc.perform(post("/api/user/question")
                        .header("Authorization", access_token)
                        .content(objectMapper.writeValueAsString
                                (new QuestionCreateDto
                                        ("", "",
                                                null)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }


    @Test
    @Sql(value = {"/script/TestQuestionResourceController/getQuestionSortedByDate/After.sql"}, executionPhase =
            Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"/script/TestQuestionResourceController/getQuestionSortedByDate/Before.sql"}, executionPhase =
            Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void getQuestionSortedByDateAndCheckFields() throws Exception {
        String access_token = getJwtToken("user100@mail.ru", "12345");
        mvc.perform(get("/api/user/question/new")
                .header("Authorization", access_token)
                .param("currentPageNumber", "1")
                .param("items", "6")
                .param("trackedTags", "100,101")
                .param("ignoredTags", "102,103,104,106")
                .param("dayFilter", "YEAR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("currentPageNumber").value(1))
                .andExpect(jsonPath("itemsOnPage").value(6))
                .andExpect(jsonPath("totalPageCount").value(1))
                .andExpect(jsonPath("totalResultCount").value(3))
                .andExpect(jsonPath("$.items[0].id").value(104))
                .andExpect(jsonPath("$.items[0].title").value("title4"))
                .andExpect(jsonPath("$.items[2].authorId").value("100"))
                .andExpect(jsonPath("$.items[2].authorReputation").value("600"))
                .andExpect(jsonPath("$.items[2].authorName").value("user100"))
                .andExpect(jsonPath("$.items[2].description").value("description0"))
                .andExpect(jsonPath("$.items[2].viewCount").value("2"))
                .andExpect(jsonPath("$.items[1].persistDateTime")
                        .value("2022-10-23T17:12:51.936176"))
                .andExpect(jsonPath("$.items[1].lastUpdateDateTime")
                        .value("2022-10-29T18:23:51.936176"))
                .andExpect(jsonPath("$.items[0].listTagDto[0].id").value(101))
                .andExpect(jsonPath("$.items[0].listTagDto[1].name").value("tag105"))
                .andExpect(jsonPath("$.items[1].listTagDto[0].description").value("it is tag 101"))
                .andExpect(jsonPath("$.items[0].isUserBookmark").value("true"));
    }

    @Test
    @Sql(value = {"/script/TestQuestionResourceController/getAllSortedQuestionDto/After.sql"}, executionPhase =
            Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"/script/TestQuestionResourceController/getAllSortedQuestionDto/Before.sql"},executionPhase =
            Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void getAllSortedQuestionDto () throws Exception {
        String access_token = getJwtToken("user100@mail.ru", "12345");
        mvc.perform(get("/api/user/question/sorted")
                .header("Authorization", access_token)
                .param("currentPageNumber","1")
                .param("itemsOnPage", "10")
                .param("dayFilter", "YEAR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("totalPageCount").value("1"))
                .andExpect(jsonPath("totalResultCount").value("7"))
                .andExpect(jsonPath("$.items[0].id").value("101"))
                .andExpect(jsonPath("$.items[6].title").value("title2"))
                .andExpect(jsonPath("$.items[1].authorId").value("101"))
                .andExpect(jsonPath("$.items[2].description").value("description0"))
                .andExpect(jsonPath("$.items[3].countAnswer").value("1"))
                .andExpect(jsonPath("$.items[4].persistDateTime")
                        .value("2022-10-26T17:24:51.936176"))
                .andExpect(jsonPath("$.items[5].id").value("106"));
    }

    @Test
    @Sql(value = {"/script/TestQuestionResourceController/getQuestionDtoNoAnswer/Before.sql"}, executionPhase =
            Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/script/TestQuestionResourceController/getQuestionDtoNoAnswer/After.sql"}, executionPhase =
            Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void checkQuestionDtoNoAnswer() throws Exception {
        String access_token = getJwtToken("user100@mail.ru", "12345");
        mvc.perform(get("/api/user/question/noAnswer")
                .header("Authorization", access_token)
                .param("page", "1")
                .param("items", "50")
                .param("ignoredTags", "100,101")
                .param("trackedTags", "102,103,104,106"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("totalPageCount").value("1"))
                .andExpect(jsonPath("totalResultCount").value("3"))
                .andExpect(jsonPath("$.items[0].id").value("103"))
                .andExpect(jsonPath("$.items[1].title").value("title5"))
                .andExpect(jsonPath("$.items[2].description").value("description6"))
                .andExpect(jsonPath("$.items[0].countAnswer").value("0"))
                .andExpect(jsonPath("$.items[1].countAnswer").value("0"))
                .andExpect(jsonPath("$.items[2].countAnswer").value("0"));
    }

    @Test
    @Sql(value = {"/script/TestQuestionResourceController/getQuestionDtoById/Before.sql"}, executionPhase =
            Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/script/TestQuestionResourceController/getQuestionDtoById/After.sql"}, executionPhase =
            Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void checkIfQuestionNotFound() throws Exception {
        String access_token = getJwtToken("user101@mail.ru", "$2a$12$RbzrY3EwB4O/UElqKDY..e8e8t88SijJ0iVtMBE0VYsQUxV6Wtvhu");
        mvc.perform(get("/api/user/question/{id}", 99)
                        .header("Authorization", access_token))
                .andExpect(status().is4xxClientError());
    }


    @Test
    @Sql(value = {"/script/TestQuestionResourceController/getCountQuestion/Before.sql"}, executionPhase =
            Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/script/TestQuestionResourceController/getCountQuestion/After.sql"}, executionPhase =
            Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void checkCountOfQuestion() throws Exception {
        String access_token = getJwtToken("user100@mail.ru", "12345");
        mvc.perform(get("/api/user/question/count")
                        .header("Authorization", access_token))
                .andExpect(MockMvcResultMatchers.content().string("7"));
    }


    @Test
    @Sql(value = "/script/TestQuestionResourceController/checkVoteQuestion/Before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/script/TestQuestionResourceController/checkVoteQuestion/After.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void checkAddVoteDown() throws Exception {
        String access_token = getJwtToken("user100@mail.ru", "12345");
        mvc.perform(post("/api/user/question/{questionId}/downVote", 101L)
                        .header("Authorization", access_token))
                .andExpect(status().isOk());
        VoteQuestion voteQuestion = em.find(VoteQuestion.class, 1L);

        Assert.assertEquals("Голосу за вопрос присвоен не правильный пользователь.",100L,voteQuestion.getUser().getId().longValue());
        Assert.assertEquals("Голосу не верно присвоен id вопроса",101L,voteQuestion.getQuestion().getId().longValue());
        Assert.assertEquals("Голосу за вопрос присвоен не правильный голос",VoteType.DOWN_VOTE,voteQuestion.getVote());

    }

    @Test
    @Sql(value = "/script/TestQuestionResourceController/checkVoteQuestion/Before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/script/TestQuestionResourceController/checkVoteQuestion/After.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void checkAddVoteUp() throws Exception {
        String access_token = getJwtToken("user100@mail.ru", "12345");
        mvc.perform(post("/api/user/question/{questionId}/upVote", 102L)
                        .header("Authorization", access_token))
                .andExpect(status().isOk());
        VoteQuestion voteQuestion = em.find(VoteQuestion.class, 2L);

        Assert.assertEquals("Голосу за вопрос присвоен не правильный пользователь.",100L,voteQuestion.getUser().getId().longValue());
        Assert.assertEquals("Голосу не верно присвоен id вопроса",102L,voteQuestion.getQuestion().getId().longValue());
        Assert.assertEquals("Голосу за вопрос присвоен не правильный голос",VoteType.UP_VOTE,voteQuestion.getVote());
    }

    @Test
    @Sql(value = "/script/TestQuestionResourceController/checkVoteQuestion/Before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/script/TestQuestionResourceController/checkVoteQuestion/After.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void checkAddVoteUpIsQuestionNotFound() throws Exception {
        String access_token = getJwtToken("user100@mail.ru", "12345");
        mvc.perform(post("/api/user/question/{questionId}/upVote", 999L)
                        .header("Authorization", access_token))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(value = "/script/TestQuestionResourceController/checkVoteQuestion/Before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/script/TestQuestionResourceController/checkVoteQuestion/After.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void checkAddVoteDownIsQuestionNotFound() throws Exception {
        String access_token = getJwtToken("user100@mail.ru", "12345");
        mvc.perform(post("/api/user/question/{questionId}/downVote", 999L)
                        .header("Authorization", access_token))
                .andExpect(status().isBadRequest());
    }
    @Test
    @Sql(value = "/script/TestQuestionResourceController/checkVoteQuestion/Before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/script/TestQuestionResourceController/checkVoteQuestion/After.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void checkAddVoteUpIsQuestionIsDeleted() throws Exception {
        String access_token = getJwtToken("user100@mail.ru", "12345");
        mvc.perform(post("/api/user/question/{questionId}/upVote", 104L)
                        .header("Authorization", access_token))
                .andExpect(status().isBadRequest());
    }
    @Test
    @Sql(value = "/script/TestQuestionResourceController/checkVoteQuestion/Before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/script/TestQuestionResourceController/checkVoteQuestion/After.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void checkAddVoteDownIsQuestionIsDeleted() throws Exception {
        String access_token = getJwtToken("user100@mail.ru", "12345");
        mvc.perform(post("/api/user/question/{questionId}/downVote", 104L)
                        .header("Authorization", access_token))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(value = {"/script/questionResourceController/getQuestionDtoByTagId/Before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/script/questionResourceController/getQuestionDtoByTagId/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetQuestionDtoByTagId() throws Exception {
        mvc.perform(get("/api/user/question/tag/101?page=1&items=5")
                        .header("Authorization", getJwtToken("vasya@mail.ru", "password")))
                .andExpect(status().isOk())
                //проверка полей <PageDto<QuestionViewDto>>
                .andExpect(jsonPath("size()").value(5))
                .andExpect(jsonPath("itemsOnPage").value(5))
                .andExpect(jsonPath("totalResultCount").value(2))
                .andExpect(jsonPath("totalPageCount").value(1))
                .andExpect(jsonPath("currentPageNumber").value(1))
                .andExpect(jsonPath("items.size()").value(2))
                //проверка полей QuestionDto
                .andExpect(jsonPath("items[0].id").value(101))
                .andExpect(jsonPath("items[0].title").value("A"))
                .andExpect(jsonPath("items[0].authorId").value(101))
                .andExpect(jsonPath("items[0].authorReputation").value(10))
                .andExpect(jsonPath("items[0].authorName").value("VasVas"))
                .andExpect(jsonPath("items[0].authorImage").value("a.b"))
                .andExpect(jsonPath("items[0].description").value("a"))
                .andExpect(jsonPath("items[0].viewCount").value(1))
                .andExpect(jsonPath("items[0].countAnswer").value(9))
                .andExpect(jsonPath("items[0].countValuable").value(5))
                .andExpect(jsonPath("items[0].persistDateTime").value("2022-10-29T00:00:00"))
                .andExpect(jsonPath("items[0].lastUpdateDateTime").value("2022-10-30T00:00:00"))
                .andExpect(jsonPath("items[0].listTagDto.size()").value(2))
                .andExpect(jsonPath("items[0].listTagDto[1].id").value(101))
                .andExpect(jsonPath("items[0].listTagDto[0].id").value(102))
                .andExpect(jsonPath("items[0].listTagDto[0].name").value("tag 2"))
                .andExpect(jsonPath("items[0].listTagDto[0].description").value("description tag 2"))
                .andExpect(jsonPath("items[0].isUserBookmark").value(true));
    }

    @Test
    @Sql(value = {"/script/questionResourceController/getQuestionDtoByTagId/Before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/script/questionResourceController/getQuestionDtoByTagId/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetQuestionDtoByTagIdNoTag() throws Exception {
        mvc.perform(get("/api/user/question/tag/110?page=1&items=5")
                        .header("Authorization", getJwtToken("vasya@mail.ru", "password")))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Sql(value = {"/script/questionResourceController/getAllQuestionsByVoteAndAnswerByInterval/Before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/script/questionResourceController/getAllQuestionsByVoteAndAnswerByInterval/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetAllQuestionsByVoteAndAnswerByMonth() throws Exception {
        mvc.perform(get("/api/user/question/sortedByMonth?currentPageNumber=1&itemsOnPage=2")
                        .header("Authorization", getJwtToken("vasya@mail.ru", "password")))
                .andExpect(status().isOk())
                //проверка полей <PageDto<QuestionViewDto>>
                .andExpect(status().isOk())
                .andExpect(jsonPath("size()").value(5))
                .andExpect(jsonPath("itemsOnPage").value(2))
                .andExpect(jsonPath("totalResultCount").value(10))
                .andExpect(jsonPath("totalPageCount").value(5))
                .andExpect(jsonPath("currentPageNumber").value(1))
                //проверка границы 30дней
                .andExpect(jsonPath("items.size()").value(10))
                //проверка полей QuestionDto
                .andExpect(jsonPath("items[0].id").value(101))
                .andExpect(jsonPath("items[1].id").value(102))
                .andExpect(jsonPath("items[0].title").value("Question 101"))
                .andExpect(jsonPath("items[0].authorId").value(101))
                .andExpect(jsonPath("items[0].authorReputation").value(10))
                .andExpect(jsonPath("items[0].authorName").value("VasVas"))
                .andExpect(jsonPath("items[0].authorImage").value("a.b"))
                .andExpect(jsonPath("items[0].description").value("description 1"))
                .andExpect(jsonPath("items[0].viewCount").value(1))
                .andExpect(jsonPath("items[0].persistDateTime").value(LocalDate.now().minusDays(7) + "T00:00:00"))
                .andExpect(jsonPath("items[0].lastUpdateDateTime").value(LocalDate.now() + "T00:00:00"))
                .andExpect(jsonPath("items[0].listTagDto.size()").value(2))
                .andExpect(jsonPath("items[0].listTagDto[1].id").value(101))
                .andExpect(jsonPath("items[0].listTagDto[0].id").value(102))
                .andExpect(jsonPath("items[0].listTagDto[0].name").value("tag 2"))
                .andExpect(jsonPath("items[0].listTagDto[0].description").value("description tag 2"))
                .andExpect(jsonPath("items[0].isUserBookmark").value(true))
                //проверка сортировки приоритет countValuable
                .andExpect(jsonPath("items[0].countAnswer").value(3))
                .andExpect(jsonPath("items[1].countAnswer").value(10))
                .andExpect(jsonPath("items[0].countValuable").value(5))
                .andExpect(jsonPath("items[1].countValuable").value(4));
    }

    @Test
    @Sql(value = {"/script/questionResourceController/getAllQuestionsByVoteAndAnswerByInterval/Before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/script/questionResourceController/getAllQuestionsByVoteAndAnswerByInterval/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetAllQuestionsByVoteAndAnswerByWeek() throws Exception {
        mvc.perform(get("/api/user/question/sortedByWeek?currentPageNumber=1&itemsOnPage=5")
                        .header("Authorization", getJwtToken("vasya@mail.ru", "password")))
                .andExpect(status().isOk())
                //проверка полей PageDto<MessageDto>
                .andExpect(status().isOk())
                .andExpect(jsonPath("size()").value(5))
                .andExpect(jsonPath("itemsOnPage").value(5))
                .andExpect(jsonPath("totalResultCount").value(7))
                .andExpect(jsonPath("totalPageCount").value(2))
                .andExpect(jsonPath("currentPageNumber").value(1))
                //проверка границы 7 дней
                .andExpect(jsonPath("items.size()").value(7));
    }



    @Test
    @Sql(value = {"/script/questionResourceController/getAllQuestionsByVoteAndAnswerByIntervalNullResult/Before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/script/questionResourceController/getAllQuestionsByVoteAndAnswerByInterval/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetAllQuestionsByVoteAndAnswerByWeekNullResult() throws Exception {
        mvc.perform(get("/api/user/question/sortedByWeek?currentPageNumber=1&itemsOnPage=5")
                        .header("Authorization", getJwtToken("vasya@mail.ru", "password")))
                .andExpect(status().isOk())
                //проверка полей PageDto<MessageDto>
                .andExpect(status().isOk())
                .andExpect(jsonPath("size()").value(5))
                .andExpect(jsonPath("itemsOnPage").value(5))
                .andExpect(jsonPath("totalResultCount").value(0))
                .andExpect(jsonPath("totalPageCount").value(0))
                .andExpect(jsonPath("currentPageNumber").value(1));
    }
}
