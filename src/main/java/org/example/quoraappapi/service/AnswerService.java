package org.example.quoraappapi.service;

import lombok.RequiredArgsConstructor;
import org.example.quoraappapi.dtos.CreateAnswerRequest;
import org.example.quoraappapi.exceptions.ResourceNotFoundException;
import org.example.quoraappapi.models.Answer;
import org.example.quoraappapi.models.Question;
import org.example.quoraappapi.models.User;
import org.example.quoraappapi.repositories.AnswerRepository;

import org.example.quoraappapi.repositories.QuestionRepository;
import org.example.quoraappapi.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;
    public Answer postAnswer(UUID questionId, CreateAnswerRequest request){
       Optional< Question >ques=questionRepository.findById(questionId);
        if(ques.isEmpty())throw new ResourceNotFoundException("Question not found");
       Optional<User> fetchedUser =userRepository.findById(request.getUserId());
       if(fetchedUser.isEmpty())throw new ResourceNotFoundException("User not found");
      Answer answer=Answer.builder()
                   .question(ques.get())
                           .user(fetchedUser.get())
                                   .text(request.getText())
                                           .build();


       return answerRepository.save(answer);
    }
public Answer editAnswer(UUID answerId, String text){
       Optional<Answer>ans=answerRepository.findById(answerId);
    if(ans.isEmpty())throw new ResourceNotFoundException("Answer not found");
    Answer existing=ans.get();
    existing.setText(text);
    return answerRepository.save(existing);
}
}
