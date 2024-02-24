package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.dao.abstracts.dto.ChatDtoDao;
import com.javamentor.qa.platform.models.dto.MessageDto;
import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.entity.MessageStar;
import com.javamentor.qa.platform.models.entity.chat.Message;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.MessageDtoService;
import com.javamentor.qa.platform.service.abstracts.model.MessageService;
import com.javamentor.qa.platform.service.abstracts.model.MessageStarService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/message")
public class MessageResourceController {

    private final MessageStarService messageStarService;
    private final MessageDtoService messageDtoService;
    private final MessageService messageService;
    private final ChatDtoDao chatDtoDao;


    @DeleteMapping("/{id}/star")
    @ApiOperation("Удаление избранного сообщения у авторизованного пользователя, используя id сообщения")
    public ResponseEntity<?> deleteStarMessageById(@PathVariable Long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        Long userId = user.getId();
        Long messageStarId = id;
        if (messageStarService.existsMessageStarByMessageIdAndUserId(messageStarId, userId)) {
            messageStarService.deleteMessageStarByMessageIdAndUserId(messageStarId, userId);
            return new ResponseEntity<>("Message with id = " + messageStarId +
                    " successfully deleted!", HttpStatus.OK);
        }
        return new ResponseEntity<>("MessageStar with id = " + messageStarId +
                " is not found to user id = " + userId, HttpStatus.NOT_FOUND);
    }

    @GetMapping("chat/{chatId}/find")
    @ApiOperation(value = "Получение PageDto с пагинацией", tags = {"Получение PageDto"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "PageDto успешно получено"),
            @ApiResponse(code = 400, message = "Параметры заданы неверно")
    })
    public ResponseEntity<List<MessageDto>> getMessagePageDto
            (@RequestParam(required = false, defaultValue = "20") int itemsOnPage, @RequestParam int currentPageNumber, @RequestParam String word, @PathVariable long chatId) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();

        Map<String, Object> paginationMap = new HashMap<>();
        paginationMap.put("class", "PaginationServiceDto");
        paginationMap.put("chatId", chatId);
        paginationMap.put("word", word);
        paginationMap.put("itemsOnPage", itemsOnPage);
        paginationMap.put("currentPageNumber", currentPageNumber);
        paginationMap.put("userId", user.getId());

        return ResponseEntity.ok(messageDtoService.getPageDto(itemsOnPage, currentPageNumber, paginationMap).getItems());
    }

    @PostMapping("/star/{id}")
    @ApiOperation("Добавление избранного сообщения у авторизованного пользователя, используя id сообщения")
    public ResponseEntity<?> addMessageStar(@PathVariable Long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        Optional<Message> message = messageService.getById(id);
        if (message.isPresent()) {
            messageStarService.persist(new MessageStar(user, message.get()));
            return new ResponseEntity<>("Message with id = " + id +
                    " successfully Add!", HttpStatus.OK);
        }
        return new ResponseEntity<>("MessageStar with id = " + id +
                " is not found to user id = " + user.getId(), HttpStatus.NOT_FOUND);
    }

    @GetMapping("/global")
    @ApiOperation(value = "вывод всех сообщений")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Сообщения найдены"),
            @ApiResponse(code = 400, message = "Сообщения не найдены")})
    public ResponseEntity<PageDto<MessageDto>> AllGlobalMessage() {
        PageDto<MessageDto> pageDto = new PageDto<>();
        List<MessageDto> allMessageFromGlobalChat = chatDtoDao.getMessageFromGlobalChat();
        pageDto.setItems(allMessageFromGlobalChat);
        return new ResponseEntity<>(pageDto, HttpStatus.OK);
    }

    @GetMapping("/global/find")
    @ApiOperation(value = "вывод сообщений с искомым текстом")
    public ResponseEntity<?> FindTextInGlobalChat(@RequestParam(defaultValue = "1") int currentPageNumber,
                                                  @RequestParam(defaultValue = "10") int itemsOnPage,
                                                  @RequestParam(defaultValue = "!#@$#%$^&*()(*&") String text) {
        Map<String, Object> map = new HashMap<>();
        map.put("class", "FindTextInGlobalChat");
        map.put("text", text);
        map.put("itemsOnPage", itemsOnPage);
        map.put("currentPageNumber", currentPageNumber);
        PageDto<MessageDto> pageDto = messageDtoService.getPageDto(currentPageNumber, itemsOnPage, map);

        return ResponseEntity.ok().body(pageDto);
    }
}

