package org.example.quoraappapi.controllers;

import lombok.RequiredArgsConstructor;
import org.example.quoraappapi.dtos.AnswerResponse;
import org.example.quoraappapi.dtos.CreateAnswerRequest;
import org.example.quoraappapi.dtos.CreateQuestionRequest;
import org.example.quoraappapi.dtos.FeedItemResponse;
import org.example.quoraappapi.dtos.FeedResponse;
import org.example.quoraappapi.service.AnswerService;
import org.example.quoraappapi.service.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/questions")
public class QuestionController {

    private final QuestionService questionService;
    private final AnswerService answerService;

    @PostMapping
    public ResponseEntity<FeedItemResponse> createQuestion(@RequestBody CreateQuestionRequest request,
                                                           Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(questionService.createQuestion(request, authentication.getName()));
    }

    @PostMapping("/{questionId}/answers")
    public ResponseEntity<AnswerResponse> createAnswer(@PathVariable UUID questionId,
                                                       @RequestBody CreateAnswerRequest request,
                                                       Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(answerService.postAnswer(questionId, request, authentication.getName()));
    }

    @GetMapping("/search")
    public FeedResponse searchQuestion(@RequestParam(required = false) String text,
                                       @RequestParam(required = false) String tag,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size) {
        return questionService.searchQuestion(text, tag, page, size);
    }
}