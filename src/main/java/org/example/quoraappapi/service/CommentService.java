package org.example.quoraappapi.service;

import lombok.RequiredArgsConstructor;
import org.example.quoraappapi.dtos.CommentResponse;
import org.example.quoraappapi.dtos.CreateCommentRequest;
import org.example.quoraappapi.exceptions.ResourceNotFoundException;
import org.example.quoraappapi.models.Answer;
import org.example.quoraappapi.models.Comment;
import org.example.quoraappapi.models.User;
import org.example.quoraappapi.repositories.AnswerRepository;
import org.example.quoraappapi.repositories.CommentRepository;
import org.example.quoraappapi.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final AnswerRepository answerRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentResponse commentOnAnswer(UUID answerId, CreateCommentRequest request, String email) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new ResourceNotFoundException("Answer not found"));
        User author = loadUser(email);
        String text = requireText(request);

        Comment comment = Comment.builder().text(text).build();
        comment.setUser(author);
        comment.setAnswer(answer);
        return CommentResponse.from(commentRepository.save(comment));
    }

    @Transactional
    public CommentResponse commentOnComment(UUID commentId, CreateCommentRequest request, String email) {
        Comment parent = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        User author = loadUser(email);
        String text = requireText(request);

        Comment comment = Comment.builder().text(text).build();
        comment.setUser(author);
        comment.setComment(parent);
        return CommentResponse.from(commentRepository.save(comment));
    }

    private User loadUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private String requireText(CreateCommentRequest request) {
        if (request.getText() == null || request.getText().isBlank()) {
            throw new IllegalArgumentException("Comment text is required");
        }
        return request.getText().trim();
    }
}