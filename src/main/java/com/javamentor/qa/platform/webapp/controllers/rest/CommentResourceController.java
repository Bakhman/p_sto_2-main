package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.entity.question.CommentQuestion;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.question.answer.CommentAnswer;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.CommentDtoService;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import com.javamentor.qa.platform.service.abstracts.model.CommentAnswerService;
import com.javamentor.qa.platform.service.abstracts.model.CommentQuestionService;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user/comment")
@Api(value = "Работа с комментариями", tags = {"Комментарии"})
public class CommentResourceController {

    private final CommentDtoService commentDtoService;
    private final QuestionDtoService questionDtoService;
    private final QuestionService questionService;
    private final CommentQuestionService commentQuestionService;
    private final AnswerService answerService;

    private final CommentAnswerService commentAnswerService;

    @GetMapping("/{id}/question")
    @ApiOperation(value = "Получение списка QuestionCommentDto по Question id",
            tags = {"список", "комментарий", "вопрос"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Список QuestionCommentDto успешно получен"),
            @ApiResponse(code = 400, message = "Вопрос с таким ID не найден")
    })
    public ResponseEntity<?> getQuestionCommentById(@PathVariable Long id) {
        return questionService.getById(id).isPresent() ?
                ResponseEntity.ok().body(questionDtoService.getQuestionCommentByQuestionId(id)) :
                ResponseEntity.badRequest().body("Question with id " + id + " not found!");
    }

    @PostMapping("/{id}/question")
    @ApiOperation(value = "Добавление комментария в вопрос", tags = {"Add comment to question"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Комментарий успешно добавлен"),
            @ApiResponse(code = 400, message = "Вопрос с таким ID не найден")
    })
    public ResponseEntity<?> addQuestionToComment(@PathVariable Long id,
                                                  @RequestBody String commentString) {
        Optional<Question> questionOptional = questionService.getById(id);
        if (questionOptional.isPresent()) {
            Question question = questionOptional.get();
            CommentQuestion commentQuestion = new CommentQuestion();
            commentQuestion.setText(commentString);
            commentQuestion.setQuestion(question);
            commentQuestionService.persist(commentQuestion);
        }
        return ResponseEntity.badRequest().body("Комментарий не был добавлен к вопросу так как вопрос с Id:" + id + "был не найден");
    }

    @GetMapping("/question/{questionId}")
    @ApiOperation(value = "Получение комментариев вопроса в нескольких страницах по Question id",
                  tags = {"список", "комментарий", "вопрос"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Список Page<CommentDto> успешно получен"),
            @ApiResponse(code = 400, message = "Вопрос с таким ID не найден")
    })
    public ResponseEntity<?> getAllCommentDtoInPages(@PathVariable long questionId,
                                                                @RequestParam(value = "currentPageNumber", defaultValue = "1") int currentPageNumber,
                                                                @RequestParam(value = "itemsOnPage", defaultValue = "10") int itemsOnPage) {
        if (questionService.getById(questionId).isPresent()) {
            Map<String, Object> objectMap = new HashMap<>();
            objectMap.put("class", "AllCommentsByQuestionId");
            objectMap.put("itemsOnPage", itemsOnPage);
            objectMap.put("currentPageNumber", currentPageNumber);
            objectMap.put("questionId", questionId);
            return ResponseEntity.ok().body(commentDtoService.getPageDto(currentPageNumber, itemsOnPage, objectMap));
        }
        return ResponseEntity.badRequest().body("Вопрос с таким ID не найден");
    }

    @GetMapping("/answer/{answerId}")
    @ApiOperation(value = "Получение комментариев ответа",tags = {"список комментариев ответа"})
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "Комментарии успешно получены"),
            @ApiResponse(code = 400,message = "Ответ с таким ID не найден")
    })
    public ResponseEntity<?> getAnswerComments(@PathVariable("answerId") Long answerId,
                                               @RequestParam(value = "currentPageNumber",defaultValue = "1") Integer currentPage,
                                               @RequestParam(value = "itemsOnPage",defaultValue = "10") Integer items ){
        if(answerService.getById(answerId).isPresent()) {
            Map<String, Object> objectMap = new HashMap<>();
            objectMap.put("itemsOnPage", items);
            objectMap.put("class", "AllCommentDtoByAnswerId");
            objectMap.put("currentPageNumber", currentPage);
            objectMap.put("answerId", answerId);

            return ResponseEntity.ok(commentDtoService.getPageDto(currentPage,items,objectMap));
        }

        return ResponseEntity.badRequest().body("Ответ с id : " + answerId + "не найден");
    }
    @PostMapping("/answer/{answerId}")
    @ApiOperation(value = "Добавление коментария в ответ", tags = {"add answer to comment"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Комментарий успешно добавлен"),
            @ApiResponse(code = 400, message = "Ответ с таким ID не найден")
    })
    public ResponseEntity<?> addAnswerToComment(@PathVariable("answerId") Long answerId,
                                                @RequestBody String text) {

        if (text.isBlank()) {
            return ResponseEntity.badRequest().body("Комментарий не должен быть пустым!");
        }
        Optional<Answer> optionalAnswer = answerService.getById(answerId);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        if (optionalAnswer.isPresent()) {
            CommentAnswer commentAnswer = new CommentAnswer();
            commentAnswer.setUser(user);
            commentAnswer.setText(text);
            commentAnswer.setAnswer(optionalAnswer.get());
            commentAnswerService.persist(commentAnswer);
            return ResponseEntity.ok("Комментарий добавлен");
        }
        return ResponseEntity.badRequest().body("Комментарий не был добавлен к вопросу так как ответ с Id:" + answerId + "был не найден.");
    }

}
