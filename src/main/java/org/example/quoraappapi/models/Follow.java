package org.example.quoraappapi.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="follows")
public class Follow extends BaseModel{
    @ManyToOne
    @JoinColumn(name="follower_id")
    private User follower;
    @ManyToOne
    @JoinColumn(name = "following_id")
    private User following;
}

