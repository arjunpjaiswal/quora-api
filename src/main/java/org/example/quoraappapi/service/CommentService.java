package org.example.quoraappapi.service;

import lombok.RequiredArgsConstructor;
import org.example.quoraappapi.dtos.CreateCommentRequest;
import org.example.quoraappapi.exceptions.ResourceNotFoundException;
import org.example.quoraappapi.models.Answer;
import org.example.quoraappapi.models.Comment;
import org.example.quoraappapi.models.User;
import org.example.quoraappapi.repositories.AnswerRepository;
import org.example.quoraappapi.repositories.CommentRepository;
import org.example.quoraappapi.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
@RequiredArgsConstructor
@Service
public class CommentService {
    private final AnswerRepository answerRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    public Comment commentOnAnswer(UUID answerId, CreateCommentRequest request){
Optional<Answer> ans=answerRepository.findById(answerId);
if(ans.isEmpty())throw new ResourceNotFoundException("Answer not found");
Answer existing=ans.get();
Comment comm=Comment.builder().text(request.getText()).build();
// need UserRepository injected
        Optional<User> user = userRepository.findById(request.getUserId());
        if(user.isEmpty()) throw new ResourceNotFoundException("User not found");
        comm.setUser(user.get());
comm.setAnswer(existing);
return commentRepository.save(comm);
    }
    public Comment commentOnComment(UUID commentId,CreateCommentRequest request){
        Optional<Comment>comment=commentRepository.findById(commentId);
        if(comment.isEmpty())throw new ResourceNotFoundException("Comment not found");
        Comment existing=comment.get();
        Comment comm=Comment.builder().text(request.getText()).build();
        comm.setComment(existing);
        Optional<User> user = userRepository.findById(request.getUserId());
        if(user.isEmpty()) throw new ResourceNotFoundException("User not found");
        comm.setUser(user.get());
        return commentRepository.save(comm);
    }
}
