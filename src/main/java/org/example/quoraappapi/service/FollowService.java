package org.example.quoraappapi.service;

import lombok.RequiredArgsConstructor;
import org.example.quoraappapi.dtos.FollowResponse;
import org.example.quoraappapi.exceptions.DuplicateResourceException;
import org.example.quoraappapi.exceptions.ResourceNotFoundException;
import org.example.quoraappapi.models.Follow;
import org.example.quoraappapi.models.User;
import org.example.quoraappapi.repositories.FollowRepository;
import org.example.quoraappapi.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    @Transactional
    public FollowResponse followUser(String currentEmail, UUID targetUserId) {
        User follower = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        User target = userRepository.findById(targetUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Target user does not exist"));

        if (follower.getId().equals(target.getId())) {
            throw new IllegalArgumentException("Cannot follow yourself");
        }
        if (followRepository.existsByFollowerIdAndFollowingId(follower.getId(), target.getId())) {
            throw new DuplicateResourceException("Already following this user");
        }

        Follow saved = followRepository.save(
                Follow.builder().follower(follower).following(target).build());

        return new FollowResponse(saved.getId(), follower.getId(), target.getId(), saved.getCreatedAt());
    }
}