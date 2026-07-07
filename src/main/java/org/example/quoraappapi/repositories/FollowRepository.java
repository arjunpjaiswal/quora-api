package org.example.quoraappapi.repositories;

import org.example.quoraappapi.models.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FollowRepository extends JpaRepository<Follow, UUID> {
}
