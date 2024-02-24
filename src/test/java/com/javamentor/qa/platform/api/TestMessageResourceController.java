package com.javamentor.qa.platform.api;

import com.javamentor.qa.platform.AbstractApiTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestMessageResourceController extends AbstractApiTest {
    // проверка работы метода с несколькими найденными соответствиями искомому тексту
    @Test
    @Sql(value = {"/script/messageResourceController/findTextInGlobalChat/Before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/script/messageResourceController/findTextInGlobalChat/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void FindTextInGlobalChatTest() throws Exception {
        mvc.perform(get("/api/user/message/global/find?currentPageNumber=1&itemsOnPage=2&text=message4")
                        .header("Authorization", getJwtToken("vasya@mail.ru", "password")))
                //проверка полей PageDto<MessageDto>
                .andExpect(status().isOk())
                .andExpect(jsonPath("size()").value(5))
                .andExpect(jsonPath("itemsOnPage").value(2))
                .andExpect(jsonPath("totalResultCount").value(8))
                .andExpect(jsonPath("totalPageCount").value(4))
                .andExpect(jsonPath("currentPageNumber").value(1))
                .andExpect(jsonPath("items.size()").value(8))

                //проверка правильности вывода поля items в PageDto<MessageDto>
                .andExpect(jsonPath("items[0].id").value(101))
                .andExpect(jsonPath("items[0].nickName").value("Vas"))
                .andExpect(jsonPath("items[0].message").value("как найти message44?"))
                .andExpect(jsonPath("items[0].image").value("a.b"))
                .andExpect(jsonPath("items[0].persistDateTime").value("2010-10-10T00:00:00"))
                .andExpect(jsonPath("items[0].userId").value(101))
                .andExpect(jsonPath("items[0].chatId").value(101))
                .andExpect(jsonPath("items[5].id").value(112))
                .andExpect(jsonPath("items[2].nickName").value("Vas4"))
                .andExpect(jsonPath("items[3].message").value("как найти message46"))
                .andExpect(jsonPath("items[4].image").value("a.b"))
                .andExpect(jsonPath("items[5].persistDateTime").value("2015-05-05T00:00:00"))
                .andExpect(jsonPath("items[7].userId").value(104))
                .andExpect(jsonPath("items[6].chatId").value(101));
    }

    //проверка работы метода с параметрами по умолчанию
    @Test
    @Sql(value = {"/script/messageResourceController/findTextInGlobalChat/Before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/script/messageResourceController/findTextInGlobalChat/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void FindTextInChatNoMatches() throws Exception {
        mvc.perform(get("/api/user/message/global/find")
                        .header("Authorization", getJwtToken("vasya@mail.ru", "password")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("size()").value(5))
                .andExpect(jsonPath("itemsOnPage").value(10))
                .andExpect(jsonPath("totalResultCount").value(0))
                .andExpect(jsonPath("totalPageCount").value(0))
                .andExpect(jsonPath("currentPageNumber").value(1))
                .andExpect(jsonPath("items.size()").value(0));
    }

    //проверка работы метода когда поиск не дал результат
    @Test
    @Sql(value = {"/script/messageResourceController/findTextInGlobalChat/Before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/script/messageResourceController/findTextInGlobalChat/After.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void FindTextInChatNullItemsOnPage() throws Exception {
        mvc.perform(get("/api/user/message/global/find?text=dfessage4")
                        .header("Authorization", getJwtToken("vasya@mail.ru", "password")))

                .andExpect(status().isOk())
                .andExpect(jsonPath("size()").value(5))
                .andExpect(jsonPath("itemsOnPage").value(10))
                .andExpect(jsonPath("totalResultCount").value(0))
                .andExpect(jsonPath("totalPageCount").value(0))
                .andExpect(jsonPath("currentPageNumber").value(1))
                .andExpect(jsonPath("items.size()").value(0));
    }
}
