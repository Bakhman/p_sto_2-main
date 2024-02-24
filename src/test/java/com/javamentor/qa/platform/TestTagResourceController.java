package com.javamentor.qa.platform;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class TestTagResourceController extends AbstractApiTest {

    @Test
    @Sql(value = {"/script/TagResourceController/GetRelatedTag/After.sql"}, executionPhase =
            Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"/script/TagResourceController/GetRelatedTag/Before.sql"}, executionPhase =
            Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void getRelatedTagDto() throws Exception {
        String access_token = getJwtToken ("vasya@mail.ru","password");
        mvc.perform(get("/api/user/tag/related")
                        .header("Authorization", access_token)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(100))
                .andExpect(jsonPath("$[0].title").value("test_tag0"))
                .andExpect(jsonPath("$[0].countQuestion").value(5))
                .andExpect(jsonPath("$[1].id").value(101))
                .andExpect(jsonPath("$[1].title").value("test_tag1"))
                .andExpect(jsonPath("$[1].countQuestion").value(4))
                .andExpect(jsonPath("$[2].id").value(102))
                .andExpect(jsonPath("$[2].title").value("test_tag2"))
                .andExpect(jsonPath("$[2].countQuestion").value(3))
                .andExpect(jsonPath("$[3].id").value(103))
                .andExpect(jsonPath("$[3].title").value("test_tag3"))
                .andExpect(jsonPath("$[3].countQuestion").value(2))
                .andExpect(jsonPath("$[4].id").value(104))
                .andExpect(jsonPath("$[4].title").value("test_tag4"))
                .andExpect(jsonPath("$[4].countQuestion").value(1));
    }

    @Test
    @Sql(value = {"/script/TagResourceController/GetAllTrackedTag/After.sql"}, executionPhase =
            Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"/script/TagResourceController/GetAllTrackedTag/Before.sql"}, executionPhase =
            Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void getAllTrackedTagDto() throws Exception {
        String access_token = getJwtToken ("vasya@mail.ru","password");
        mvc.perform(get("/api/user/tag/tracked")
                        .header("Authorization", access_token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(100))
                .andExpect(jsonPath("$[0].name").value("test_tag0"))
                .andExpect(jsonPath("$[1].id").value(101))
                .andExpect(jsonPath("$[1].name").value("test_tag1"))
                .andExpect(jsonPath("$[2].id").doesNotExist());
    }

    @Test
    @Sql(value = {"/script/TagResourceController/GetAllIgnoredTag/After.sql"}, executionPhase =
            Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"/script/TagResourceController/GetAllIgnoredTag/Before.sql"}, executionPhase =
            Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void getAllIgnoredTagDto() throws Exception {
        String access_token = getJwtToken ("vasya@mail.ru","password");
        mvc.perform(get("/api/user/tag/ignored")
                        .header("Authorization", access_token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(100))
                .andExpect(jsonPath("$[0].name").value("test_tag0"))
                .andExpect(jsonPath("$[1].id").value(101))
                .andExpect(jsonPath("$[1].name").value("test_tag1"))
                .andExpect(jsonPath("$[2].id").doesNotExist());;
    }


    //Тесты метода addTrackedTag
    @Test
    @Sql(value = {"/script/TagResourceController/AddTrackedTag/After.sql"}, executionPhase =
            Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"/script/TagResourceController/AddTrackedTag/Before.sql"}, executionPhase =
            Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void addTrackedTagAlreadyTracked() throws Exception {
        String access_token = getJwtToken ("user100@mail.ru","12345");
        mvc.perform(post("/api/user/tag/{id}/tracked", 100)
                        .header("Authorization", access_token))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Tag with id found in tracked"));
    }

    @Test
    @Sql(value = {"/script/TagResourceController/AddTrackedTag/After.sql"}, executionPhase =
            Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"/script/TagResourceController/AddTrackedTag/Before.sql"}, executionPhase =
            Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void addTrackedTagIgnoredTag() throws Exception {
        String access_token = getJwtToken ("user100@mail.ru","12345");
        mvc.perform(post("/api/user/tag/{id}/tracked", 101)
                        .header("Authorization", access_token))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Tag with id found in ignored"));
    }

    @Test
    @Sql(value = {"/script/TagResourceController/AddTrackedTag/After.sql"}, executionPhase =
            Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"/script/TagResourceController/AddTrackedTag/Before.sql"}, executionPhase =
            Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void addTrackedTagNotFoundTag() throws Exception {
        String access_token = getJwtToken("user100@mail.ru", "12345");
        mvc.perform(post("/api/user/tag/{id}/tracked", 104)
                        .header("Authorization", access_token))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Tag with this ID was not found"));
    }

    @Test
    @Sql(value = {"/script/TagResourceController/AddTrackedTag/After.sql"}, executionPhase =
            Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"/script/TagResourceController/AddTrackedTag/Before.sql"}, executionPhase =
            Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void addTrackedTagSuccessful() throws Exception {
        String access_token = getJwtToken ("user100@mail.ru","12345");
        mvc.perform(post("/api/user/tag/{id}/tracked", 102)
                        .header("Authorization", access_token))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$[0].id").value(100))
                .andExpect(jsonPath("$[0].name").value("some tag 100"))
                .andExpect(jsonPath("$[0].description").value("some description"))

                .andExpect(jsonPath("$[1].id").value(102))
                .andExpect(jsonPath("$[1].name").value("some tag 102"))
                .andExpect(jsonPath("$[0].description").value("some description"));

    }

    //Тесты метода addTrackedTag
    @Test
    @Sql(value = {"/script/TagResourceController/AddIgnoredTag/After.sql"}, executionPhase =
            Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"/script/TagResourceController/AddIgnoredTag/Before.sql"}, executionPhase =
            Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void addIgnoredTagAlreadyIgnored() throws Exception {
        String access_token = getJwtToken ("user100@mail.ru","12345");
        mvc.perform(post("/api/user/tag/{id}/ignored", 101)
                        .header("Authorization", access_token))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Tag with id found in ignored"));
    }

    @Test
    @Sql(value = {"/script/TagResourceController/AddIgnoredTag/After.sql"}, executionPhase =
            Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"/script/TagResourceController/AddIgnoredTag/Before.sql"}, executionPhase =
            Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void addIgnoredTagAlreadyTracked() throws Exception {
        String access_token = getJwtToken ("user100@mail.ru","12345");
        mvc.perform(post("/api/user/tag/{id}/ignored", 100)
                        .header("Authorization", access_token))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Tag with id found in tracked"));
    }

    @Test
    @Sql(value = {"/script/TagResourceController/AddIgnoredTag/After.sql"}, executionPhase =
            Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"/script/TagResourceController/AddIgnoredTag/Before.sql"}, executionPhase =
            Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void addIgnoredTagNotFoundTag() throws Exception {
        String access_token = getJwtToken("user100@mail.ru", "12345");
        mvc.perform(post("/api/user/tag/{id}/ignored", 104)
                        .header("Authorization", access_token))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Tag with this ID was not found"));
    }

    @Test
    @Sql(value = {"/script/TagResourceController/AddIgnoredTag/After.sql"}, executionPhase =
            Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"/script/TagResourceController/AddIgnoredTag/Before.sql"}, executionPhase =
            Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void addIgnoredTagSuccessful() throws Exception {
        String access_token = getJwtToken ("user100@mail.ru","12345");
        mvc.perform(post("/api/user/tag/{id}/ignored", 102)
                        .header("Authorization", access_token))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$[0].id").value(101))
                .andExpect(jsonPath("$[0].name").value("some tag 101"))
                .andExpect(jsonPath("$[0].description").value("some description"))

                .andExpect(jsonPath("$[1].id").value(102))
                .andExpect(jsonPath("$[1].name").value("some tag 102"))
                .andExpect(jsonPath("$[0].description").value("some description"));

    }
}