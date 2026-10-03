package org.example.quoraappapi.service;

import lombok.RequiredArgsConstructor;
import org.example.quoraappapi.dtos.LikeResponse;
import org.example.quoraappapi.exceptions.DuplicateResourceException;
import org.example.quoraappapi.exceptions.ResourceNotFoundException;
import org.example.quoraappapi.models.*;
import org.example.quoraappapi.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;

    @Transactional
    public LikeResponse like(String type, UUID targetId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Like like = switch (type) {
            case "questions" -> {
                Question question = questionRepository.findById(targetId)
                        .orElseThrow(() -> new ResourceNotFoundException("Question not found"));
                if (likeRepository.findByUserIdAndQuestionId(user.getId(), targetId).isPresent()) {
                    throw new DuplicateResourceException("You already liked this question");
                }
                yield Like.builder().user(user).question(question).build();
            }
            case "answers" -> {
                Answer answer = answerRepository.findById(targetId)
                        .orElseThrow(() -> new ResourceNotFoundException("Answer not found"));
                if (likeRepository.findByUserIdAndAnswerId(user.getId(), targetId).isPresent()) {
                    throw new DuplicateResourceException("You already liked this answer");
                }
                yield Like.builder().user(user).answer(answer).build();
            }
            case "comments" -> {
                Comment comment = commentRepository.findById(targetId)
                        .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
                if (likeRepository.findByUserIdAndCommentId(user.getId(), targetId).isPresent()) {
                    throw new DuplicateResourceException("You already liked this comment");
                }
                yield Like.builder().user(user).comment(comment).build();
            }
            default -> throw new IllegalArgumentException("Invalid type. Use questions, answers or comments");
        };

        return LikeResponse.from(likeRepository.save(like));
    }

    @Transactional
    public void unlike(String type, UUID targetId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Optional<Like> existing = switch (type) {
            case "questions" -> likeRepository.findByUserIdAndQuestionId(user.getId(), targetId);
            case "answers" -> likeRepository.findByUserIdAndAnswerId(user.getId(), targetId);
            case "comments" -> likeRepository.findByUserIdAndCommentId(user.getId(), targetId);
            default -> throw new IllegalArgumentException("Invalid type. Use questions, answers or comments");
        };

        likeRepository.delete(existing
                .orElseThrow(() -> new ResourceNotFoundException("You have not liked this item")));
    }
}