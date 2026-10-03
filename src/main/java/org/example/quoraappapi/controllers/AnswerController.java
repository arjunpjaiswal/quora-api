package org.example.quoraappapi.controllers;

import lombok.RequiredArgsConstructor;
import org.example.quoraappapi.dtos.AnswerResponse;
import org.example.quoraappapi.dtos.UpdateAnswerRequest;
import org.example.quoraappapi.service.AnswerService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/answers")
public class AnswerController {

    private final AnswerService answerService;

    @PutMapping("/{answerId}")
    public AnswerResponse updateAnswer(@PathVariable UUID answerId,
                                       @RequestBody UpdateAnswerRequest request,
                                       Authentication authentication) {
        return answerService.editAnswer(answerId, request.getText(), authentication.getName());
    }
}