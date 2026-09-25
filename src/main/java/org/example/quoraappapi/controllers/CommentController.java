package org.example.quoraappapi.controllers;

import lombok.RequiredArgsConstructor;
import org.example.quoraappapi.dtos.CreateCommentRequest;
import org.example.quoraappapi.models.Comment;
import org.example.quoraappapi.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
   @PostMapping("/answers/{answerId}/comments")
    public ResponseEntity<Comment>commentOnAnswer(@PathVariable UUID answerId,@RequestBody CreateCommentRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.commentOnAnswer(answerId,request));
   }
    @PostMapping("/comments/{commentId}/comments")
    public ResponseEntity<Comment>commentOnComment(@PathVariable UUID commentId,@RequestBody CreateCommentRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.commentOnComment(commentId,request));
    }

}
