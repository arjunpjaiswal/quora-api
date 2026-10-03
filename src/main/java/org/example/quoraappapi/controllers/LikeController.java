package org.example.quoraappapi.controllers;

import lombok.RequiredArgsConstructor;
import org.example.quoraappapi.dtos.LikeResponse;
import org.example.quoraappapi.service.LikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{type}/{id}/likes")
    public ResponseEntity<LikeResponse> like(@PathVariable String type,
                                             @PathVariable UUID id,
                                             Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(likeService.like(type, id, authentication.getName()));
    }

    @DeleteMapping("/{type}/{id}/likes")
    public ResponseEntity<Void> unlike(@PathVariable String type,
                                       @PathVariable UUID id,
                                       Authentication authentication) {
        likeService.unlike(type, id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}