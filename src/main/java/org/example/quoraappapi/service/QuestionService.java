package org.example.quoraappapi.service;

import lombok.RequiredArgsConstructor;
import org.example.quoraappapi.dtos.CreateQuestionRequest;
import org.example.quoraappapi.dtos.FeedItemResponse;
import org.example.quoraappapi.dtos.FeedResponse;
import org.example.quoraappapi.exceptions.ResourceNotFoundException;
import org.example.quoraappapi.models.Question;
import org.example.quoraappapi.models.Topic;
import org.example.quoraappapi.models.User;
import org.example.quoraappapi.repositories.QuestionRepository;
import org.example.quoraappapi.repositories.TopicRepository;
import org.example.quoraappapi.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;

    @Transactional
    public FeedItemResponse createQuestion(CreateQuestionRequest request, String email) {
        User author = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }

        List<Topic> topics = new ArrayList<>();
        if (request.getTopicTags() != null) {
            for (String tag : request.getTopicTags()) {
                if (tag == null || tag.isBlank()) continue;
                String name = tag.trim();
                Topic topic = topicRepository.findByName(name)
                        .orElseGet(() -> topicRepository.save(Topic.builder().name(name).build()));
                if (!topics.contains(topic)) topics.add(topic);
            }
        }

        Question question = Question.builder()
                .user(author)
                .title(request.getTitle())
                .body(request.getBody())
                .topics(topics)
                .build();

        return FeedItemResponse.from(questionRepository.save(question));
    }

    @Transactional(readOnly = true)
    public FeedResponse searchQuestion(String text, String tag, int page, int size) {
        String cleanText = (text == null || text.isBlank()) ? null : text.trim();
        String cleanTag = (tag == null || tag.isBlank()) ? null : tag.trim();

        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.min(Math.max(size, 1), 50),
                Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Question> result = questionRepository.searchQuestions(cleanText, cleanTag, pageable);
        return FeedResponse.from(result);
    }
}