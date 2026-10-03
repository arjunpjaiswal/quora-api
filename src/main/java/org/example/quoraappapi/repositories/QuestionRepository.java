package org.example.quoraappapi.repositories;

import org.example.quoraappapi.models.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface QuestionRepository extends JpaRepository<Question, UUID> {
    @Query("SELECT q FROM Question q LEFT JOIN q.topics t WHERE " +
            "(q.title LIKE %:text% OR q.body LIKE %:text%) " +
            "AND (:tag IS NULL OR t.name = :tag)")
    List<Question> searchQuestions(@Param("text") String text, @Param("tag") String tag);
    @Query("""
    SELECT q FROM Question q
    WHERE q.user.id IN (
        SELECT f.following.id FROM Follow f
        WHERE f.follower.email = :email
    )
    """)
    Page<Question> findFeedByFollowerEmail(@Param("email") String email, Pageable pageable);
}
