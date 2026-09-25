package org.example.quoraappapi.service;

import lombok.RequiredArgsConstructor;
import org.example.quoraappapi.models.Topic;
import org.example.quoraappapi.repositories.TopicRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TopicService {
    private final TopicRepository topicRepository;
    public Topic createTopic(Topic topic){
      return topicRepository.save(topic);
    }
    public List<Topic>getTopics(){
        List<Topic>topics=topicRepository.findAll();
        return topics;
    }
}
