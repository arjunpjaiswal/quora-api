package org.example.quoraappapi.dtos;

import org.example.quoraappapi.models.User;

import java.time.Instant;
import java.util.UUID;

public record MyProfileResponse(UUID id, String userName, String email, String bio, Instant createdAt) {

    public static MyProfileResponse from(User u) {
        return new MyProfileResponse(u.getId(), u.getUserName(), u.getEmail(), u.getBio(), u.getCreatedAt());
    }
}