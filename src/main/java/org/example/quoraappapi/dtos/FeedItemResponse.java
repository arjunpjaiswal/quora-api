package org.example.quoraappapi.dtos;

import org.example.quoraappapi.models.Question;
import org.example.quoraappapi.models.Topic;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record FeedItemResponse(
        UUID id,
        String title,
        String body,
        UUID authorId,
        String authorName,
        List<String> topics,
        Instant createdAt) {

    public static FeedItemResponse from(Question q) {
        List<String> topicNames = q.getTopics() == null
                ? List.of()
                : q.getTopics().stream().map(Topic::getName).toList();

        return new FeedItemResponse(q.getId(), q.getTitle(), q.getBody(),
                q.getUser().getId(), q.getUser().getUserName(),
                topicNames, q.getCreatedAt());
    }
}