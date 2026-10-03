package org.example.quoraappapi.dtos;

import java.util.List;

public record FeedResponse(
        List<FeedItemResponse> items,
        int page,
        int size,
        long totalElements,
        boolean hasNext) {}
