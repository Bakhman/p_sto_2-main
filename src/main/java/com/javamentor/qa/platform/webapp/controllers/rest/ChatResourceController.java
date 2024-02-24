package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.ChatDto;
import com.javamentor.qa.platform.models.dto.CreateGroupChatDto;
import com.javamentor.qa.platform.models.dto.CreateSingleChatDto;
import com.javamentor.qa.platform.models.dto.GroupChatDto;
import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.SingleChatDto;
import com.javamentor.qa.platform.models.entity.chat.Chat;
import com.javamentor.qa.platform.models.entity.chat.ChatType;
import com.javamentor.qa.platform.models.entity.chat.GroupChat;
import com.javamentor.qa.platform.models.entity.chat.SingleChat;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.ChatDtoService;
import com.javamentor.qa.platform.service.abstracts.dto.MessageDtoService;
import com.javamentor.qa.platform.service.abstracts.model.ChatService;
import com.javamentor.qa.platform.service.abstracts.model.GroupChatService;
import com.javamentor.qa.platform.service.abstracts.model.SingleChatService;
import com.javamentor.qa.platform.service.abstracts.model.UserChatPinService;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import com.javamentor.qa.platform.webapp.converters.ChatConverter;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/chat")
public class ChatResourceController {
    private final ChatDtoService chatDtoService;
    private final SingleChatService singleChatService;
    private final ChatConverter chatConverter;
    private final MessageDtoService messageDtoService;
    private final GroupChatService groupChatService;
    private final UserService userService;
    private final ChatService chatService;
    private final UserChatPinService userChatPinService;

    @GetMapping()
    @ApiOperation(value = "Получение всех ChatDto по строке", tags = {"Get All ChatDto ByString"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Все ChatDto получены"),
            @ApiResponse(code = 400, message = "ChatDto не найдены")
    })
    public ResponseEntity<PageDto<ChatDto>> getChatByString(@RequestParam("currentPageNumber") int currentPageNumber,
                                                            @RequestParam(value = "itemsOnPage", defaultValue = "10", required = false) int itemsOnPage,
                                                            @RequestParam("string") String string) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        Map<String, Object> param = new HashMap<>();
        param.put("class", "GetChatsByString");
        param.put("currentPageNumber", currentPageNumber);
        param.put("itemsOnPage", itemsOnPage);
        param.put("user", user);
        param.put("string", string);
        return ResponseEntity.ok().body(chatDtoService.getPageDto(currentPageNumber, itemsOnPage, param));
    }
    @GetMapping("/single")
    public ResponseEntity<List<SingleChatDto>> getAllOfSingleChatDto() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        return ResponseEntity.ok().body(chatDtoService.getAllSingleChatDto(user.getId()));
    }

    @PostMapping("/single")
    @ApiOperation(value = "Создание SingleChat", tags = {"SingleChat"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SingleChat успешно создан"),
            @ApiResponse(code = 400, message = "Ошибка создания SingleChat")})
    public ResponseEntity<?> addSingleChat(@Valid @RequestBody CreateSingleChatDto createSingleChatDto) {
        SingleChat singleChat = chatConverter.createSingleChatDtoToSingleChat(createSingleChatDto);
        User userOne = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        Optional<User> userTwo = userService.getById(createSingleChatDto.getUserId());
        singleChat.setUserOne(userOne);
        singleChat.setUseTwo(userTwo.get());
        singleChatService.addSingleChatAndMessage(singleChat, createSingleChatDto.getMessage());
        return ResponseEntity.ok().body(createSingleChatDto);
    }

    @GetMapping("/{id}/single/message")
    @ApiOperation(value = "Получение сообщений с сортировкой по дате и фильтрации по тексту", tags = {"Get messages sorted by date and filter by text"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Сообщения получены"),
            @ApiResponse(code = 400, message = "Ошибка получения сообщений")})
    public ResponseEntity<?> getPaginationMessagesSortedDate (@RequestParam(defaultValue = "10") int itemsOnPage,
                                                              @RequestParam int currentPageNumber,
                                                              @RequestParam(defaultValue = "") String text,
                                                              @PathVariable long id) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();

        Map<String, Object> paginationMap = new HashMap<>();
        paginationMap.put("class", "MessageDtoSortedByDate");
        paginationMap.put("chatId", id);
        paginationMap.put("itemsOnPage", itemsOnPage);
        paginationMap.put("currentPageNumber", currentPageNumber);
        paginationMap.put("userId", user.getId());
        paginationMap.put("message",text);

        return ResponseEntity.ok(messageDtoService.getPageDto(itemsOnPage, currentPageNumber, paginationMap).getItems());
    }

    @GetMapping("/group")
    @ApiOperation(value = "Получение всех MessageDto с пагинацией и сортировкой по времени оправки",
            tags = {"Get Sorted by time MessageDto"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Все MessageDto получены"),
            @ApiResponse(code = 400, message = "MessageDto не найдены")
    })
    public ResponseEntity<?> getAllSortedByDateMessageDto(@RequestParam(defaultValue = "30") int itemsOnPage,
                                                          @RequestParam int currentPageNumber,
                                                          @RequestParam Long chatId) {

        Optional<GroupChatDto> o = chatDtoService.getGroupChatByIdWithPaginationMessage(itemsOnPage, currentPageNumber, chatId);
        if (o.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(o.get());
    }

    @PostMapping("/group")
    @ApiOperation(value = "Добавление пользователей в групповой чат",
            tags = {"Add users to groupChat"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Пользователи успешно добавлены"),
            @ApiResponse(code = 400, message = "Произошла ошибка добавления пользователей")
    })
    public ResponseEntity<?> addToGroupChat(CreateGroupChatDto createGroupChatDto) {

        // Сет юзеров по id из dto
        Set<User> usersFromDto = new HashSet<>(userService.getAllByIds(createGroupChatDto.getUserIds()));

        //Сет id найденных юзеров
        Set<Long> detectedUsersIds = new HashSet<>();
        usersFromDto.stream().forEach(user -> detectedUsersIds.add(user.getId()));

        /* если оба сета не равны по размеру, то вычитаем
        из большего меньшее, в остатке id ненайденных юзеров*/
        if (usersFromDto.size() != createGroupChatDto.getUserIds().size()) {
            Set<Long> lostUsersIds = new HashSet<>(createGroupChatDto.getUserIds());
            lostUsersIds.removeAll(detectedUsersIds);
            return ResponseEntity.badRequest().body("Пользователи с id= " + lostUsersIds + " не найдены");
        }
        groupChatService.persist(new GroupChat(usersFromDto, createGroupChatDto.getChatName()));
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Удаление чата по ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Чат удален"),
            @ApiResponse(code = 404, message = "Чат с указанным ID не найден")})
    @PutMapping("/{id}")
    public ResponseEntity<String> deleteChatById(@PathVariable("id") Long id) {
        Optional<Chat> optionalChat = chatService.getById(id);

        if (optionalChat.isEmpty()) {
            return new ResponseEntity<>("Chat is not exist", HttpStatus.NOT_FOUND);
        }
        if (optionalChat.get().getChatType() == ChatType.SINGLE) {
            singleChatService.deleteByChatId(id);
            return ResponseEntity.ok().body("SingleChat successfully deleted");
        }
        groupChatService.deleteGroupChatByChatId(id);
        return ResponseEntity.ok().body("GroupChat successfully deleted");
    }

    @ApiOperation(value = "Добавление пользователя в групповой чат",
            tags = {"Добавление пользователя в групповой чат"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Пользователь добавлен"),
            @ApiResponse(code = 404, message = "Чат/Пользователь не найден")})
    @PostMapping("/group/{id}/join")
    public ResponseEntity<String> joinUserByUserChat(@PathVariable("id") Long chatId, @RequestBody Long userId) {
        groupChatService.addUserToGroupChat(chatId, userId);
        return ResponseEntity.ok("Пользователь успешно добавлен");
    }

    @ApiOperation(value = "Изменение картинки в групповом чате")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Изображение успешно обновлено"),
            @ApiResponse(code = 404, message = "Групповой чат с указанным ID не найден")})
    @PatchMapping("/{id}/group/image")
    public ResponseEntity<String> updateImageGroupChat(@PathVariable("id") Long id,
                                                       @RequestBody String updatedImageLink) {
        Optional<GroupChat> optionalGroupChat = groupChatService.getById(id);
        if (optionalGroupChat.isEmpty()) {
            return new ResponseEntity<>("Group chat is not exist", HttpStatus.NOT_FOUND);
        }
        GroupChat groupChat = optionalGroupChat.get();
        groupChat.setImageLink(updatedImageLink);
        groupChatService.update(groupChat);
        return ResponseEntity.ok().body("Image successfully update");
    }

    @ApiOperation(value = "Удаление пользователя из группового чата")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Пользователь успешно удален"),
            @ApiResponse(code = 404, message = "Групповой чат с указанным ID не найден")})
    @DeleteMapping("/{groupId}/group")
    public ResponseEntity<String> deleteUserFromGroupChat(@PathVariable("groupId") Long groupChatId,
                                                          @RequestParam(value = "userId") Long userId) {
        Optional<GroupChat> optionalGroupChat = groupChatService.getById(groupChatId);
        if (optionalGroupChat.isEmpty()) {
            return new ResponseEntity<>("Group chat is not exist", HttpStatus.NOT_FOUND);
        }
        groupChatService.deleteUserFromGroupChatById(groupChatId, userId);
        return ResponseEntity.ok().body("User successfully delete from group chat");
    }

    @ApiOperation(value = "Получение сообщений группового чата по его ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Сообщения группового чат получены"),
            @ApiResponse(code = 400, message = "Пользователь не состоит в групповом чате"),
            @ApiResponse(code = 404, message = "Чат не найден.")})
    @GetMapping("/{id}/group/message")
    public ResponseEntity<?> getPaginationGroupChatMessagesSortedDate(@RequestParam(defaultValue = "10") int itemsOnPage,
                                                                  @RequestParam int currentPageNumber,
                                                                      @PathVariable long id,
                                                                  @RequestParam(required = false,defaultValue = "") String message) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        Optional<GroupChat> groupChat;
        //Решение в 2 запроса.Знаю как написать запрос, но не могу его найти в проекте.
        groupChat = groupChatService.getById(id);
        if( groupChatService.chatVerifyUser(user.getId(),id) && groupChat.isPresent()){
            Map<String, Object> paginationMap = new HashMap<>();
            paginationMap.put("class", "MessageDtoSortedByDate");
            paginationMap.put("chatId", id);
            paginationMap.put("itemsOnPage", itemsOnPage);
            paginationMap.put("currentPageNumber", currentPageNumber);
            paginationMap.put("message",message);
            paginationMap.put("userId", user.getId());

            return ResponseEntity.ok(messageDtoService.getPageDto(itemsOnPage, currentPageNumber, paginationMap).getItems());
        }
        return ResponseEntity.status(404).body("Чат не найден.");

    }

    @DeleteMapping("/{id}/single")
    @ApiOperation(value = "Удаление пользователя в Single Chat", tags = {"SingleChat"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Пользователь успешно удалён из чата."),
            @ApiResponse(code = 400, message = "Single Chat с данным ID не найден."),
            @ApiResponse(code = 404, message = "Single Chat для данного пользователя уже удален.")})
    public ResponseEntity<?> deleteUserFromSingleChat(@PathVariable("id") Long id) {
        Optional<SingleChat> singleChat = singleChatService.getById(id);
        if (singleChat.isPresent()) {
            singleChatService.deleteByChatId(id);
            return new ResponseEntity<>( "Пользователь успешно удалён из чата", HttpStatus.OK);
        }
        return new ResponseEntity<>("Single Chat с данным ID не найден.", HttpStatus.BAD_REQUEST);

    }
}
