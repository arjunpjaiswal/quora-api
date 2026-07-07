package org.example.quoraappapi.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comment extends BaseModel{
    @Column(nullable = false)
    private String text;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name="answer_id")
    private Answer answer;
    @ManyToOne
    @JoinColumn(name="parent_comment_id")
    private Comment comment;
}
