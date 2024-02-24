package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.BookmarksDto;
import com.javamentor.qa.platform.models.dto.UserProfileQuestionDto;
import com.javamentor.qa.platform.service.abstracts.dto.AnswerDtoService;
import com.javamentor.qa.platform.service.abstracts.dto.BookmarksDtoService;
import com.javamentor.qa.platform.service.abstracts.dto.UserDtoService;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/{userId}/profile")
public class ProfileUserResourceController {

    private final UserDtoService userDtoService;
    private final UserService userService;
    private final BookmarksDtoService bookmarksDtoService;
    private final AnswerDtoService answerDtoService;

    @GetMapping("/delete/questions")
    @ApiOperation("Получение удаленных вопросов пользователя")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Получен список удаленных вопросов"),
            @ApiResponse(code = 400, message = "Для данного пользователя список удаленных вопросов отсутствует"),
            @ApiResponse(code = 404, message = "Неверный ID пользователя"),
    })
    public ResponseEntity<?> getDeletedQuestionsByUser(@PathVariable("userId") long userId) {
        List<UserProfileQuestionDto> deletedQuestionsByUserId = userDtoService.getAllDeletedQuestionsByUserId(userId);
        if (!userService.existsById(userId)) {
            return new ResponseEntity<>("Неверный ID пользователя", HttpStatus.NOT_FOUND);
        }
        return deletedQuestionsByUserId.isEmpty() ?
                new ResponseEntity<>("Для данного пользователя список удаленных вопросов отсутствует", HttpStatus.BAD_REQUEST) :
                new ResponseEntity<>(deletedQuestionsByUserId, HttpStatus.OK);

    }

    @GetMapping("/questions")
    @ApiOperation("Получение вопросов пользователя")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Получен список вопросов"),
            @ApiResponse(code = 400, message = "Для данного пользователя список вопросов отсутствует"),
            @ApiResponse(code = 404, message = "Неверный ID пользователя"),
    })
    public ResponseEntity<?> getQuestionsByUser(@PathVariable("userId") long userId) {
        List<UserProfileQuestionDto> listUserProfileQuestionDto = userDtoService.getUserProfileQuestionDtoAddByUserId(userId);
        if (!userService.existsById(userId)) {
            return new ResponseEntity<>("Неверный ID пользователя", HttpStatus.NOT_FOUND);
        }
        return listUserProfileQuestionDto.isEmpty() ?
                new ResponseEntity<>("Для данного пользователя список вопросов отсутствует", HttpStatus.BAD_REQUEST) :
                new ResponseEntity<>(listUserProfileQuestionDto, HttpStatus.OK);
    }

    @GetMapping("/bookmarks")
    @ApiOperation(value = "Получение всех закладок BookmarksDto в профиле пользователя")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Закладки не найдены"),
            @ApiResponse(code = 404, message = "Неверный ID пользователя")
    })
    public ResponseEntity<?> getBookmarksDtoByUserId(@PathVariable("userId") long userId) {
        List<BookmarksDto> bookmarksDto = bookmarksDtoService.getBookmarksDtoByUserId(userId);
        if (!userService.existsById(userId)) {
            return new ResponseEntity<>("Неверный ID пользователя", HttpStatus.NOT_FOUND);
        }
        return bookmarksDto.isEmpty() ?
                new ResponseEntity<>("Закладки не найдены", HttpStatus.BAD_REQUEST) :
                new ResponseEntity<>(bookmarksDto, HttpStatus.OK);
    }

    @GetMapping("/question/week")
    @ApiOperation(value = "Получение количества ответов от пользователя")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Получено количество ответов"),
            @ApiResponse(code = 400, message = "Ответы не найдены"),
            @ApiResponse(code = 404, message = "Неверный ID пользователя")
    })
    public ResponseEntity<?> getCountOfAnswersByUserToWeek(@PathVariable("userId") long userId) {
        Integer answerCount = answerDtoService.getCountOfAnswersByUserToWeek(userId);
        return new ResponseEntity<>(answerCount, HttpStatus.OK);
    }
}
