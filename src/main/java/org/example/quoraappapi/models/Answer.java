package org.example.quoraappapi.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="answers")
public class Answer extends BaseModel{
    @ManyToOne // one user may have many answers
    @JoinColumn(name = "user_id")
   private User user;
    @Column(nullable = false)
   private String text;
   @ManyToOne // one question may have many answers
   @JoinColumn(name = "question_id")
   private  Question question;
}
