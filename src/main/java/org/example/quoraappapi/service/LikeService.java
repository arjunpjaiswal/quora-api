package org.example.quoraappapi.service;

import lombok.RequiredArgsConstructor;
import org.example.quoraappapi.dtos.CreateLikeRequest;
import org.example.quoraappapi.exceptions.ResourceNotFoundException;
import org.example.quoraappapi.models.*;
import org.example.quoraappapi.repositories.*;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    public Like likeEntity(String type, UUID id, CreateLikeRequest request){
        switch(type) {
            case "questions" ->
            {
                Optional<Question>ques=questionRepository.findById(id);
                if(ques.isEmpty())throw new ResourceNotFoundException("Question not found");
                Optional<User> user=userRepository.findById(request.getUserId());
                if(user.isEmpty())throw new ResourceNotFoundException("User Not found!!");
                User existingUser=user.get();
                Question existingQuestion=ques.get();
                Like like=Like.builder()
                        .user(existingUser)
                        .question(existingQuestion)
                        .build();


                return likeRepository.save(like);

            }
            case "answers" ->{
                // fetch answer, set on like
                Optional<Answer>ans=answerRepository.findById(id);
                if(ans.isEmpty())throw new ResourceNotFoundException("Answer not found");
                Answer existingAnswer=ans.get();
                Optional<User> user=userRepository.findById(request.getUserId());
                if(user.isEmpty())throw new ResourceNotFoundException("User Not found!!");
                User existingUser=user.get();
                Like like=Like.builder()
                        .user(existingUser)
                        .answer(existingAnswer)
                        .build();


                return likeRepository.save(like);
            }
            case "comments" -> {
                // fetch comment, set on like
                Optional<Comment>comm=commentRepository.findById(id);
                if(comm.isEmpty())throw new ResourceNotFoundException("Comment not found");
                Comment existingComment=comm.get();
                Optional<User> user=userRepository.findById(request.getUserId());
                if(user.isEmpty())throw new ResourceNotFoundException("User not found");
                User existingUser=user.get();
                Like like=Like.builder()
                        .user(existingUser)
                        .comment(existingComment)
                        .build();


                return likeRepository.save(like);

            }
            default -> throw new ResourceNotFoundException("Invalid type");
        }
    }
}
