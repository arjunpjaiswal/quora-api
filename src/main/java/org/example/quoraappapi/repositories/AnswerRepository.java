package org.example.quoraappapi.repositories;

import org.example.quoraappapi.models.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AnswerRepository extends JpaRepository<Answer, UUID> {
}
