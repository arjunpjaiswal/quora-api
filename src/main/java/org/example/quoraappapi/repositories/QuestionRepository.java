package org.example.quoraappapi.repositories;

import org.example.quoraappapi.models.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface QuestionRepository extends JpaRepository<Question, UUID> {

    @Query("""
        SELECT q FROM Question q
        WHERE (:text IS NULL
               OR LOWER(q.title) LIKE LOWER(CONCAT('%', :text, '%'))
               OR LOWER(q.body) LIKE LOWER(CONCAT('%', :text, '%')))
          AND (:tag IS NULL
               OR EXISTS (SELECT t FROM q.topics t WHERE LOWER(t.name) = LOWER(:tag)))
        """)
    Page<Question> searchQuestions(@Param("text") String text,
                                   @Param("tag") String tag,
                                   Pageable pageable);

    @Query("""
        SELECT q FROM Question q
        WHERE q.user.id IN (
            SELECT f.following.id FROM Follow f
            WHERE f.follower.email = :email
        )
        """)
    Page<Question> findFeedByFollowerEmail(@Param("email") String email, Pageable pageable);
}