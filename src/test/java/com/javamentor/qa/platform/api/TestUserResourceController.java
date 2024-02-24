package com.javamentor.qa.platform.api;

import com.javamentor.qa.platform.AbstractApiTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


public class TestUserResourceController extends AbstractApiTest {
 //Top 10 проверка на кол-во < 10, порядок, граница неделя
    @Test
    @Sql(value = {"/script/UserResourceController/getTop10ByAnswerAndCountAnswerByUser/Before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/script/UserResourceController/getTop10ByAnswerAndCountAnswerByUser/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetTop10ByAnswerPerWeek() throws Exception {

        mvc.perform(get("/api/user/top/answer/week")
                        .header("Authorization", getJwtToken("vasya@mail.ru", "password")))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].id").value(101))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].email").value("vasya@mail.ru"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].reputation").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].reputation").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].reputation").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].fullName").value("Vas"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].linkImage").value("a.b"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].city").value("city"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].dateRegister").value("2010-10-10T00:00:00"));
    }

    //test null Answer
    @Test
    @Sql(value = {"/script/UserResourceController/getTop10ByAnswerAndCountAnswerByUser/BeforeNullAnswer.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/script/UserResourceController/getTop10ByAnswerAndCountAnswerByUser/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

    public void testGetTop10ByAnswerPerWeekNull() throws Exception {

        mvc.perform(get("/api/user/top/answer/week")
                        .header("Authorization", getJwtToken("vasya@mail.ru", "password")))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(0));
    }

    //проверка на кол-во 10=10, граница месяц
    @Test
    @Sql(value = {"/script/UserResourceController/getTop10ByAnswerAndCountAnswerByUser/Before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/script/UserResourceController/getTop10ByAnswerAndCountAnswerByUser/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetTop10ByAnswerPerMonth() throws Exception {

        mvc.perform(get("/api/user/top/answer/month")
                        .header("Authorization", getJwtToken("vasya@mail.ru", "password")))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].id").value(101))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].email").value("vasya@mail.ru"));
    }

    //проверка на кол-во >10, граница год
    @Test
    @Sql(value = {"/script/UserResourceController/getTop10ByAnswerAndCountAnswerByUser/Before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/script/UserResourceController/getTop10ByAnswerAndCountAnswerByUser/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetTop10ByAnswerPerYear() throws Exception {
        mvc.perform(get("/api/user/top/answer/year")
                        .header("Authorization", getJwtToken("vasya@mail.ru", "password")))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(101))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("vasya@mail.ru"));
    }


    @Test
    @Sql(value = {"/script/UserResourceController/getUserById/Before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/script/UserResourceController/getUserById/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getUserByIdTest() throws Exception {
        mvc.perform(get("/api/user/{userId}", 100)
                        .header("Authorization", getJwtToken("user101@mail.ru", "Password!2"))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.email").value("user100@mail.ru"))
                .andExpect(jsonPath("$.fullName").value("user100"))
                .andExpect(jsonPath("$.linkImage").value("\\image_user100.jpg"))
                .andExpect(jsonPath("$.city").value("user100_city"))
                .andExpect(jsonPath("$.reputation").value("5"))
                .andExpect(jsonPath("$.dateRegister", is(notNullValue())));

        mvc.perform(get("/api/user/{userId}", 106)
                        .header("Authorization", getJwtToken("user101@mail.ru", "Password!2"))
                )
                .andDo(print())
                .andExpect(status().is(404))
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string("User with id 106 not found!"));

    }

    @Test
    @Sql(value = {"/script/UserResourceController/getUserByReg/Before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/script/UserResourceController/getUserByReg/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getUserByRegTest() throws Exception {
        mvc.perform(get("/api/user/new")
                        .header("Authorization", getJwtToken("user101@mail.ru", "Password!2"))
                        .param("currentPageNumber", "1")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.currentPageNumber").value("1"))
                .andExpect(jsonPath("$.totalPageCount").value("2"))
                .andExpect(jsonPath("$.totalResultCount").value("12"))
                .andExpect(jsonPath("$.items[0].id").value(100))
                .andExpect(jsonPath("$.items[0].email").value("user100@mail.ru"))
                .andExpect(jsonPath("$.items[0].dateRegister").value("2022-01-12T17:05:24.197649"))
                .andExpect(jsonPath("$.items[1].id").value(101))
                .andExpect(jsonPath("$.items[1].email").value("user101@mail.ru"))
                .andExpect(jsonPath("$.items[1].dateRegister").value("2022-02-12T17:05:24.197649"))
                .andExpect(jsonPath("$.items[2].id").value(102))
                .andExpect(jsonPath("$.items[2].email").value("user102@mail.ru"))
                .andExpect(jsonPath("$.items[2].dateRegister").value("2022-03-12T17:05:24.197649"))
                .andExpect(jsonPath("$.items[3].id").value(103))
                .andExpect(jsonPath("$.items[3].email").value("user103@mail.ru"))
                .andExpect(jsonPath("$.items[3].dateRegister").value("2022-04-12T17:05:24.197649"))
                .andExpect(jsonPath("$.items[4].id").value(104))
                .andExpect(jsonPath("$.items[4].email").value("user104@mail.ru"))
                .andExpect(jsonPath("$.items[4].dateRegister").value("2022-05-12T17:05:24.197649"))
                .andExpect(jsonPath("$.items[5].id").value(105))
                .andExpect(jsonPath("$.items[5].email").value("user105@mail.ru"))
                .andExpect(jsonPath("$.items[5].dateRegister").value("2022-06-12T17:05:24.197649"))
                .andExpect(jsonPath("$.items[6].id").value(106))
                .andExpect(jsonPath("$.items[6].email").value("user106@mail.ru"))
                .andExpect(jsonPath("$.items[6].dateRegister").value("2022-07-12T17:05:24.197649"))
                .andExpect(jsonPath("$.items[7].id").value(107))
                .andExpect(jsonPath("$.items[7].email").value("user107@mail.ru"))
                .andExpect(jsonPath("$.items[7].dateRegister").value("2022-08-12T17:05:24.197649"))
                .andExpect(jsonPath("$.items[8].id").value(108))
                .andExpect(jsonPath("$.items[8].email").value("user108@mail.ru"))
                .andExpect(jsonPath("$.items[8].dateRegister").value("2022-09-12T17:05:24.197649"))
                .andExpect(jsonPath("$.items[9].id").value(109))
                .andExpect(jsonPath("$.items[9].email").value("user109@mail.ru"))
                .andExpect(jsonPath("$.items[9].dateRegister").value("2022-10-12T17:05:24.197649"))
                .andExpect(jsonPath("$.itemsOnPage").value("10"));

        mvc.perform(get("/api/user/new")
                        .header("Authorization", getJwtToken("user101@mail.ru", "Password!2"))
                        .param("currentPageNumber", "2")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.currentPageNumber").value("2"))
                .andExpect(jsonPath("$.totalPageCount").value("2"))
                .andExpect(jsonPath("$.totalResultCount").value("12"))
                .andExpect(jsonPath("$.items[0].id").value(110))
                .andExpect(jsonPath("$.items[0].email").value("user110@mail.ru"))
                .andExpect(jsonPath("$.items[0].dateRegister").value("2022-11-12T17:05:24.197649"))
                .andExpect(jsonPath("$.items[1].id").value(111))
                .andExpect(jsonPath("$.items[1].email").value("user111@mail.ru"))
                .andExpect(jsonPath("$.items[1].dateRegister").value("2022-12-12T17:05:24.197649"))
                .andExpect(jsonPath("$.itemsOnPage").value("10"));

        mvc.perform(get("/api/user/new")
                        .header("Authorization", getJwtToken("user101@mail.ru", "Password!2"))
                        .param("currentPageNumber", "3")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.currentPageNumber").value("3"))
                .andExpect(jsonPath("$.totalPageCount").value("2"))
                .andExpect(jsonPath("$.totalResultCount").value("12"))
                .andExpect(jsonPath("$.items").isEmpty())
                .andExpect(jsonPath("$.itemsOnPage").value("10"));

        mvc.perform(get("/api/user/new")
                        .header("Authorization", getJwtToken("user101@mail.ru", "Password!2"))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Required int parameter 'currentPageNumber' is not present"));

    }

    @Test
    @Sql(value = {"/script/UserResourceController/getAllUserDtoSortReputation/Before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/script/UserResourceController/getAllUserDtoSortReputation/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllUserDtoSortReputationTest() throws Exception {
        mvc.perform(get("/api/user/reputation")
                        .header("Authorization", getJwtToken("user101@mail.ru", "Password!2"))
                        .param("currentPageNumber", "1")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.currentPageNumber").value("1"))
                .andExpect(jsonPath("$.totalPageCount").value("2"))
                .andExpect(jsonPath("$.totalResultCount").value("12"))
                .andExpect(jsonPath("$.items[0].id").value(104))
                .andExpect(jsonPath("$.items[0].email").value("user104@mail.ru"))
                .andExpect(jsonPath("$.items[0].reputation").value("11"))
                .andExpect(jsonPath("$.items[1].id").value(106))
                .andExpect(jsonPath("$.items[1].email").value("user106@mail.ru"))
                .andExpect(jsonPath("$.items[1].reputation").value("9"))
                .andExpect(jsonPath("$.items[2].id").value(110))
                .andExpect(jsonPath("$.items[2].email").value("user110@mail.ru"))
                .andExpect(jsonPath("$.items[2].reputation").value("8"))
                .andExpect(jsonPath("$.items[3].id").value(108))
                .andExpect(jsonPath("$.items[3].email").value("user108@mail.ru"))
                .andExpect(jsonPath("$.items[3].reputation").value("8"))
                .andExpect(jsonPath("$.items[4].id").value(107))
                .andExpect(jsonPath("$.items[4].email").value("user107@mail.ru"))
                .andExpect(jsonPath("$.items[4].reputation").value("7"))
                .andExpect(jsonPath("$.items[5].id").value(100))
                .andExpect(jsonPath("$.items[5].email").value("user100@mail.ru"))
                .andExpect(jsonPath("$.items[5].reputation").value("6"))
                .andExpect(jsonPath("$.items[6].id").value(101))
                .andExpect(jsonPath("$.items[6].email").value("user101@mail.ru"))
                .andExpect(jsonPath("$.items[6].reputation").value("5"))
                .andExpect(jsonPath("$.items[7].id").value(109))
                .andExpect(jsonPath("$.items[7].email").value("user109@mail.ru"))
                .andExpect(jsonPath("$.items[7].reputation").value("5"))
                .andExpect(jsonPath("$.items[8].id").value(103))
                .andExpect(jsonPath("$.items[8].email").value("user103@mail.ru"))
                .andExpect(jsonPath("$.items[8].reputation").value("3"))
                .andExpect(jsonPath("$.items[9].id").value(105))
                .andExpect(jsonPath("$.items[9].email").value("user105@mail.ru"))
                .andExpect(jsonPath("$.items[9].reputation").value("2"))
                .andExpect(jsonPath("$.itemsOnPage").value("10"));

        mvc.perform(get("/api/user/reputation")
                        .header("Authorization", getJwtToken("user101@mail.ru", "Password!2"))
                        .param("currentPageNumber", "2")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.currentPageNumber").value("2"))
                .andExpect(jsonPath("$.totalPageCount").value("2"))
                .andExpect(jsonPath("$.totalResultCount").value("12"))
                .andExpect(jsonPath("$.items[0].id").value(102))
                .andExpect(jsonPath("$.items[0].email").value("user102@mail.ru"))
                .andExpect(jsonPath("$.items[0].reputation").value("1"))
                .andExpect(jsonPath("$.items[1].id").value(111))
                .andExpect(jsonPath("$.items[1].email").value("user111@mail.ru"))
                .andExpect(jsonPath("$.items[1].reputation").value("0"))
                .andExpect(jsonPath("$.itemsOnPage").value("10"));

        mvc.perform(get("/api/user/reputation")
                        .header("Authorization", getJwtToken("user101@mail.ru", "Password!2"))
                        .param("currentPageNumber", "3")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.currentPageNumber").value("3"))
                .andExpect(jsonPath("$.totalPageCount").value("2"))
                .andExpect(jsonPath("$.totalResultCount").value("12"))
                .andExpect(jsonPath("$.items").isEmpty())
                .andExpect(jsonPath("$.itemsOnPage").value("10"));

        mvc.perform(get("/api/user/reputation")
                        .header("Authorization", getJwtToken("user101@mail.ru", "Password!2"))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Required int parameter 'currentPageNumber' is not present"));

    }

    @Test
    @Sql(value = {"/script/UserResourceController/getAllUserDtoSortDTO/Before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/script/UserResourceController/getAllUserDtoSortDTO/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllUserDtoSortDTOTest() throws Exception {
        mvc.perform(get("/api/user/vote")
                        .header("Authorization", getJwtToken("user101@mail.ru", "Password!2"))
                        .param("currentPageNumber", "1")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.currentPageNumber").value("1"))
                .andExpect(jsonPath("$.totalPageCount").value("2"))
                .andExpect(jsonPath("$.totalResultCount").value("12"))
                .andExpect(jsonPath("$.items[0].id").value(106))
                .andExpect(jsonPath("$.items[0].email").value("user106@mail.ru"))
                .andExpect(jsonPath("$.items[1].id").value(101))
                .andExpect(jsonPath("$.items[1].email").value("user101@mail.ru"))
                .andExpect(jsonPath("$.items[2].id").value(108))
                .andExpect(jsonPath("$.items[2].email").value("user108@mail.ru"))
                .andExpect(jsonPath("$.items[3].id").value(100))
                .andExpect(jsonPath("$.items[3].email").value("user100@mail.ru"))
                .andExpect(jsonPath("$.items[4].id").value(104))
                .andExpect(jsonPath("$.items[4].email").value("user104@mail.ru"))
                .andExpect(jsonPath("$.items[5].id").value(107))
                .andExpect(jsonPath("$.items[5].email").value("user107@mail.ru"))
                .andExpect(jsonPath("$.items[6].id").value(105))
                .andExpect(jsonPath("$.items[6].email").value("user105@mail.ru"))
                .andExpect(jsonPath("$.items[7].id").value(109))
                .andExpect(jsonPath("$.items[7].email").value("user109@mail.ru"))
                .andExpect(jsonPath("$.items[8].id").value(102))
                .andExpect(jsonPath("$.items[8].email").value("user102@mail.ru"))
                .andExpect(jsonPath("$.items[9].id").value(111))
                .andExpect(jsonPath("$.items[9].email").value("user111@mail.ru"))
                .andExpect(jsonPath("$.itemsOnPage").value("10"));

        mvc.perform(get("/api/user/vote")
                        .header("Authorization", getJwtToken("user101@mail.ru", "Password!2"))
                        .param("currentPageNumber", "2")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.currentPageNumber").value("2"))
                .andExpect(jsonPath("$.totalPageCount").value("2"))
                .andExpect(jsonPath("$.totalResultCount").value("12"))
                .andExpect(jsonPath("$.items[0].id").value(103))
                .andExpect(jsonPath("$.items[0].email").value("user103@mail.ru"))
                .andExpect(jsonPath("$.items[1].id").value(110))
                .andExpect(jsonPath("$.items[1].email").value("user110@mail.ru"))
                .andExpect(jsonPath("$.itemsOnPage").value("10"));

        mvc.perform(get("/api/user/vote")
                        .header("Authorization", getJwtToken("user101@mail.ru", "Password!2"))
                        .param("currentPageNumber", "3")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.currentPageNumber").value("3"))
                .andExpect(jsonPath("$.totalPageCount").value("2"))
                .andExpect(jsonPath("$.totalResultCount").value("12"))
                .andExpect(jsonPath("$.items").isEmpty())
                .andExpect(jsonPath("$.itemsOnPage").value("10"));

        mvc.perform(get("/api/user/vote")
                        .header("Authorization", getJwtToken("user101@mail.ru", "Password!2"))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Required int parameter 'currentPageNumber' is not present"));
    }

    @Test
    @Sql(value = {"/script/UserResourceController/updatePasswordByEmail/Before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/script/UserResourceController/updatePasswordByEmail/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updatePasswordByEmailSuccessTest() throws Exception {
        String password = "Password!2";
        mvc.perform(put("/api/user/100/change/password")
                        .header("Authorization", getJwtToken("user100@mail.ru", "Password!1"))
                        .content(password))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Пароль пользователя изменён"));
    }

    @Test
    @Sql(value = {"/script/UserResourceController/updatePasswordByEmail/Before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/script/UserResourceController/updatePasswordByEmail/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updatePasswordByEmailValidationFailureTest() throws Exception {
        String password = "Password!";
        mvc.perform(put("/api/user/100/change/password")
                        .header("Authorization", getJwtToken("user100@mail.ru", "Password!1"))
                        .content(password))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Пароль должен содержать цифры"));

        String password1 = "Password1";
        mvc.perform(put("/api/user/100/change/password")
                        .header("Authorization", getJwtToken("user100@mail.ru", "Password!1"))
                        .content(password1))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Пароль должен содержать спецсимволы"));

        String password2 = "PASSWORD!1";
        mvc.perform(put("/api/user/100/change/password")
                        .header("Authorization", getJwtToken("user100@mail.ru", "Password!1"))
                        .content(password2))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Пароль должен содержать строчные буквы"));

        String password3 = "password!1";
        mvc.perform(put("/api/user/100/change/password")
                        .header("Authorization", getJwtToken("user100@mail.ru", "Password!1"))
                        .content(password3))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Пароль должен содержать прописные буквы"));

        String password4 = "Pas!1";
        mvc.perform(put("/api/user/100/change/password")
                        .header("Authorization", getJwtToken("user100@mail.ru", "Password!1"))
                        .content(password4))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Пароль должен содержать не менее 6 символов"));
    }

    @Test
    @Sql(value = {"/script/UserResourceController/updatePasswordByEmail/Before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/script/UserResourceController/updatePasswordByEmail/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updatePasswordByEmailSamePasswordTest() throws Exception {
        String password = "Password!1";
        mvc.perform(put("/api/user/100/change/password")
                        .header("Authorization", getJwtToken("user100@mail.ru", "Password!1"))
                        .content(password))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Новый пароль совпадает с текущим"));
    }

    @Test
    @Sql(value = {"/script/UserResourceController/updatePasswordByEmail/Before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/script/UserResourceController/updatePasswordByEmail/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updatePasswordByEmailWrongIdTest() throws Exception {
        String password = "Password!2";
        mvc.perform(put("/api/user/110/change/password")
                        .header("Authorization", getJwtToken("user100@mail.ru", "Password!1"))
                        .content(password))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Неверный ID пользователя"));
    }
}


