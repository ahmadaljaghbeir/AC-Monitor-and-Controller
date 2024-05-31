package org.example.acmc.model;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long role_id;

    @Column(nullable = false)
    private String role;
}