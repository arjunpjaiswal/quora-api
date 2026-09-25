package org.example.quoraappapi.controllers;

import lombok.RequiredArgsConstructor;
import org.example.quoraappapi.dtos.CreateAnswerRequest;
import org.example.quoraappapi.dtos.CreateQuestionRequest;
import org.example.quoraappapi.models.Answer;
import org.example.quoraappapi.models.Question;
import org.example.quoraappapi.service.AnswerService;
import org.example.quoraappapi.service.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import   java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/questions")
public class QuestionController {
    private final QuestionService questionService;
    private final AnswerService answerService;
    @PostMapping
    public ResponseEntity<Question> createQuestion(@RequestBody CreateQuestionRequest request){
return ResponseEntity.status(HttpStatus.CREATED).body(questionService.createQuestion(request));

    }

    @PostMapping("/{questionId}/answers")
    public ResponseEntity<Answer>createAnswer(@RequestBody CreateAnswerRequest request
            , @PathVariable UUID questionId){
        return ResponseEntity.status(HttpStatus.CREATED).body(answerService.postAnswer(questionId,request));
    }
    @GetMapping("/search")
    public List<Question> searchQuestion(
            @RequestParam(required = false)String text,
            @RequestParam(required=false)String tag){
        return questionService.searchQuestion(text,tag);
    }

}
