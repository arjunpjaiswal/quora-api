package org.example.quoraappapi.dtos;

import org.example.quoraappapi.models.Like;

import java.time.Instant;
import java.util.UUID;

public record LikeResponse(UUID id, String type, UUID targetId, UUID userId, Instant createdAt) {

    public static LikeResponse from(Like l) {
        String type;
        UUID target;
        if (l.getQuestion() != null) {
            type = "question";
            target = l.getQuestion().getId();
        } else if (l.getAnswer() != null) {
            type = "answer";
            target = l.getAnswer().getId();
        } else {
            type = "comment";
            target = l.getComment().getId();
        }
        return new LikeResponse(l.getId(), type, target, l.getUser().getId(), l.getCreatedAt());
    }
}