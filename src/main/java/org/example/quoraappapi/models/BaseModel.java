package org.example.quoraappapi.models;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    protected UUID id; // Fixed: Changed Long to UUID to match GenerationType.UUID

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    protected Instant createdAt; // Updated: Replaced Date with Instant (No @Temporal needed)

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    protected Instant updatedAt; // Updated: Replaced Date with Instant (No @Temporal needed)
}
