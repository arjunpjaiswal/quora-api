package org.example.quoraappapi.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="topics")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Topic extends BaseModel{
    @Column(nullable = false)
    private String name;
    @ManyToMany(mappedBy = "topics")
    private List<Question> questions;
}
