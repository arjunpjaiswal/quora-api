package org.example.quoraappapi.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Optional;
@Entity
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User extends BaseModel{
    @Column(nullable = false,unique=true)
    @OneToMany(mappedBy = "user")
    private List<Question> questions;
    private String userName;
    @Column(nullable = false,unique = true)
    private String email;
    private String bio;
}
