package org.example.quoraappapi.repositories;

import org.example.quoraappapi.models.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TopicRepository extends JpaRepository<Topic, UUID> {
    Optional<Topic> findByName(String tagName);

}
