package org.example.quoraappapi.controllers;

import lombok.RequiredArgsConstructor;
import org.example.quoraappapi.dtos.CreateAnswerRequest;
import org.example.quoraappapi.models.Answer;
import org.example.quoraappapi.service.AnswerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/answers")
public class AnswerController {
    private final AnswerService answerService;
    @PutMapping("/{answerId}")
    public Answer updateAnswer(@RequestBody String text,@PathVariable UUID answerId){
      return  answerService.editAnswer(answerId,text);
    }
}
