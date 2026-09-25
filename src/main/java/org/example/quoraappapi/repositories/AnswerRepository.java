package org.example.quoraappapi.repositories;

import org.example.quoraappapi.models.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface AnswerRepository extends JpaRepository<Answer, UUID> {
    Optional<Answer >findById(UUID answerId);
}
