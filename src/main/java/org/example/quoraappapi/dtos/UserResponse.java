package org.example.quoraappapi.dtos;

import org.example.quoraappapi.models.User;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(UUID id, String userName, String bio, Instant createdAt) {

    public static UserResponse from(User u) {
        return new UserResponse(u.getId(), u.getUserName(), u.getBio(), u.getCreatedAt());
    }
}