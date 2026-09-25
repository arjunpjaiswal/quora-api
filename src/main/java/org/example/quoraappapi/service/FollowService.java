package org.example.quoraappapi.service;

import lombok.RequiredArgsConstructor;
import org.example.quoraappapi.exceptions.ResourceNotFoundException;
import org.example.quoraappapi.models.Follow;
import org.example.quoraappapi.models.User;
import org.example.quoraappapi.repositories.FollowRepository;
import org.example.quoraappapi.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
  public Follow followUser(UUID userId, UUID targetUserId){
      if(userId.equals(targetUserId)) throw new RuntimeException("Cannot follow yourself");
      Optional<User>user=userRepository.findById(userId);
      Optional<User>targetUser=userRepository.findById(targetUserId);
      if(user.isEmpty())throw new ResourceNotFoundException("User not found");
      if(targetUser.isEmpty())throw new ResourceNotFoundException("target user does not exists !!");
      User existingUser=user.get();
      User existingTargetUser=targetUser.get();
      Follow follow = Follow.builder()
              .follower(existingUser)
              .following(existingTargetUser)
              .build();
      return followRepository.save(follow);
  }
}
