package org.example.quoraappapi.dtos;

import org.example.quoraappapi.models.Answer;

import java.time.Instant;
import java.util.UUID;

public record AnswerResponse(
        UUID id,
        UUID questionId,
        UUID authorId,
        String authorName,
        String text,
        Instant createdAt,
        Instant updatedAt) {

    public static AnswerResponse from(Answer a) {
        return new AnswerResponse(a.getId(), a.getQuestion().getId(),
                a.getUser().getId(), a.getUser().getUserName(),
                a.getText(), a.getCreatedAt(), a.getUpdatedAt());
    }
}