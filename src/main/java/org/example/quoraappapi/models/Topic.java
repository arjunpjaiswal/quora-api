package org.example.quoraappapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @Column(nullable = false,unique = true)
    private String name;
    @JsonIgnore
    @ManyToMany(mappedBy = "topics")
    private List<Question> questions;
}
