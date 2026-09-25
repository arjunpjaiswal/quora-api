package org.example.quoraappapi.controllers;

import lombok.RequiredArgsConstructor;
import org.example.quoraappapi.models.Topic;
import org.example.quoraappapi.service.TopicService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class TopicController {
    private final TopicService topicService;
    @PostMapping("/topics")
    public ResponseEntity<Topic>createTopic(@RequestBody Topic topic){
        return ResponseEntity.status(HttpStatus.CREATED).body(topicService.createTopic(topic));
    }
    @GetMapping("/topics")
    public List<Topic> getTopics() {
        return topicService.getTopics();
    }
}
