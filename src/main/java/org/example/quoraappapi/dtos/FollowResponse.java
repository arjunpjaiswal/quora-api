package org.example.quoraappapi.dtos;


import java.time.Instant;
import java.util.UUID;

public record FollowResponse(UUID id, UUID followerId, UUID followingId, Instant createdAt) {}