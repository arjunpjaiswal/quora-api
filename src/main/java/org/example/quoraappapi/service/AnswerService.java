package org.example.quoraappapi.service;

import lombok.RequiredArgsConstructor;
import org.example.quoraappapi.dtos.AnswerResponse;
import org.example.quoraappapi.dtos.CreateAnswerRequest;
import org.example.quoraappapi.exceptions.ForbiddenException;
import org.example.quoraappapi.exceptions.ResourceNotFoundException;
import org.example.quoraappapi.models.Answer;
import org.example.quoraappapi.models.Question;
import org.example.quoraappapi.models.User;
import org.example.quoraappapi.repositories.AnswerRepository;
import org.example.quoraappapi.repositories.QuestionRepository;
import org.example.quoraappapi.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;

    @Transactional
    public AnswerResponse postAnswer(UUID questionId, CreateAnswerRequest request, String email) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));
        User author = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (request.getText() == null || request.getText().isBlank()) {
            throw new IllegalArgumentException("Answer text is required");
        }

        Answer answer = Answer.builder()
                .question(question)
                .user(author)
                .text(request.getText().trim())
                .build();

        return AnswerResponse.from(answerRepository.save(answer));
    }

    @Transactional
    public AnswerResponse editAnswer(UUID answerId, String text, String email) {
        Answer existing = answerRepository.findById(answerId)
                .orElseThrow(() -> new ResourceNotFoundException("Answer not found"));

        if (!existing.getUser().getEmail().equals(email)) {
            throw new ForbiddenException("You can only edit your own answers");
        }
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Answer text is required");
        }

        existing.setText(text.trim());
        return AnswerResponse.from(answerRepository.save(existing));
    }
}