package org.example.quoraappapi.controllers;

import lombok.RequiredArgsConstructor;
import org.example.quoraappapi.models.Follow;
import org.example.quoraappapi.service.FollowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class FollowController {
private final FollowService followService;
@PostMapping("/users/{userId}/follow/{targetUserId}")
    public ResponseEntity<Follow>followUser(@PathVariable UUID userId,@PathVariable UUID targetUserId){
    return ResponseEntity.status(HttpStatus.CREATED).body(followService.followUser(userId,targetUserId));
}
}
