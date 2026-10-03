package org.example.quoraappapi.dtos;


import org.example.quoraappapi.models.Comment;

import java.time.Instant;
import java.util.UUID;

public record CommentResponse(
        UUID id,
        UUID answerId,          // set when the comment is on an answer
        UUID parentCommentId,   // set when the comment is a reply
        UUID authorId,
        String authorName,
        String text,
        Instant createdAt) {

    public static CommentResponse from(Comment c) {
        return new CommentResponse(
                c.getId(),
                c.getAnswer() == null ? null : c.getAnswer().getId(),
                c.getComment() == null ? null : c.getComment().getId(),
                c.getUser().getId(),
                c.getUser().getUserName(),
                c.getText(),
                c.getCreatedAt());
    }
}
