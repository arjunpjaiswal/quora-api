package org.example.quoraappapi.service;

import lombok.RequiredArgsConstructor;
import org.example.quoraappapi.dtos.FeedItemResponse;
import org.example.quoraappapi.dtos.FeedResponse;
import org.example.quoraappapi.models.Question;
import org.example.quoraappapi.repositories.QuestionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final QuestionRepository questionRepository;

    @Transactional(readOnly = true)
    public FeedResponse getFeed(String email, int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 50);

        Pageable pageable = PageRequest.of(safePage, safeSize,
                Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Question> result = questionRepository.findFeedByFollowerEmail(email, pageable);

        List<FeedItemResponse> items = result.getContent().stream()
                .map(FeedItemResponse::from)
                .toList();

        return new FeedResponse(items, result.getNumber(), result.getSize(),
                result.getTotalElements(), result.hasNext());
    }
}