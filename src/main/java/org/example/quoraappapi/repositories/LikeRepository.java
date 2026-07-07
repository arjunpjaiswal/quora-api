package org.example.quoraappapi.repositories;

import org.example.quoraappapi.models.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LikeRepository extends JpaRepository<Like, UUID> {
}
