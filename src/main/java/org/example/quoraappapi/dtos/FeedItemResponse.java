package org.example.quoraappapi.dtos;

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
        Instant createdAt) {}
