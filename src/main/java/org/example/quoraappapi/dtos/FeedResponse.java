package org.example.quoraappapi.dtos;

import org.example.quoraappapi.models.Question;
import org.springframework.data.domain.Page;

import java.util.List;

public record FeedResponse(
        List<FeedItemResponse> items,
        int page,
        int size,
        long totalElements,
        boolean hasNext) {

    public static FeedResponse from(Page<Question> page) {
        return new FeedResponse(
                page.getContent().stream().map(FeedItemResponse::from).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.hasNext());
    }
}