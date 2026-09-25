package org.example.quoraappapi.controllers;

import lombok.RequiredArgsConstructor;
import org.example.quoraappapi.dtos.CreateLikeRequest;
import org.example.quoraappapi.models.Like;
import org.example.quoraappapi.service.LikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/api/v1")
@RequiredArgsConstructor
@RestController
public class LikeController {
    private final LikeService likeService;
    @PostMapping("/{type}/{id}/likes")
    public ResponseEntity<Like> likeEntity(
            @PathVariable String type,
            @PathVariable UUID id,
            @RequestBody CreateLikeRequest request){
     return   ResponseEntity.status(HttpStatus.CREATED).body(likeService.likeEntity(type,id,request));
    }
}
