package org.example.quoraappapi.service;

import lombok.RequiredArgsConstructor;
import org.example.quoraappapi.dtos.CreateQuestionRequest;
import org.example.quoraappapi.exceptions.ResourceNotFoundException;
import org.example.quoraappapi.models.Question;
import org.example.quoraappapi.models.Topic;
import org.example.quoraappapi.models.User;
import org.example.quoraappapi.repositories.QuestionRepository;
import org.example.quoraappapi.repositories.TopicRepository;
import org.example.quoraappapi.repositories.UserRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;
    public   Question createQuestion(CreateQuestionRequest request){
Optional<User> fetchedUser=userRepository.findById(request.getUserId());
if(fetchedUser.isEmpty()) throw new ResourceNotFoundException("User not found");
List<String>fetchedTopicTags=request.getTopicTags();
List<Topic>fetchedTopics=new ArrayList<>();
for(String topic:fetchedTopicTags){
    Optional<Topic>existingTopic=topicRepository.findByName(topic);
    Topic topics = existingTopic.isPresent() ? existingTopic.get() : topicRepository.save(Topic.builder().name(topic).build());
    fetchedTopics.add(topics);

}
Question question=Question.builder()
        .user(fetchedUser.get())
        .title(request.getTitle())
        .body(request.getBody()).
        topics(fetchedTopics).build();
return questionRepository.save(question);
    }
    public List<Question> searchQuestion(String text,String tag){
       return  questionRepository.searchQuestions(text,tag);
          }

}
