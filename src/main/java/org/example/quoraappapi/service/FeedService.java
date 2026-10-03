package org.example.quoraappapi.service;

import lombok.RequiredArgsConstructor;
import org.example.quoraappapi.dtos.FeedItemResponse;
import org.example.quoraappapi.dtos.FeedResponse;
import org.example.quoraappapi.models.Question;
import org.example.quoraappapi.models.Topic;
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
        int safeSize = Math.min(Math.max(size, 1), 50);   // cap so nobody asks for 1,000,000 rows

        Pageable pageable = PageRequest.of(safePage, safeSize,
                Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Question> result = questionRepository.findFeedByFollowerEmail(email, pageable);

        List<FeedItemResponse> items = result.getContent().stream()
                .map(this::toDto)
                .toList();

        return new FeedResponse(items, result.getNumber(), result.getSize(),
                result.getTotalElements(), result.hasNext());
    }

    private FeedItemResponse toDto(Question q) {
        List<String> topicNames = q.getTopics() == null
                ? List.of()
                : q.getTopics().stream().map(Topic::getName).toList();

        return new FeedItemResponse(q.getId(), q.getTitle(), q.getBody(),
                q.getUser().getId(), q.getUser().getUserName(),
                topicNames, q.getCreatedAt());
    }
}
