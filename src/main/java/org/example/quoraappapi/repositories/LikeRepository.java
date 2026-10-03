package org.example.quoraappapi.repositories;

import org.example.quoraappapi.models.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LikeRepository extends JpaRepository<Like, UUID> {
    Optional<Like> findByUserIdAndQuestionId(UUID userId, UUID questionId);
    Optional<Like> findByUserIdAndAnswerId(UUID userId, UUID answerId);
    Optional<Like> findByUserIdAndCommentId(UUID userId, UUID commentId);
}