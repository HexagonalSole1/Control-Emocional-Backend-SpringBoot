package com.example.APISkeleton.persistance.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "job_likes")
public class JobLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job; // 🔹 Empleo al que se dio like

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 🔹 Usuario que dio like
}
