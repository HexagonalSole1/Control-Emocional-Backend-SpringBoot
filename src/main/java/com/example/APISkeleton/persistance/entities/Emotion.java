package com.example.APISkeleton.persistance.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "emotions")
@Getter
@Setter
public class Emotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private String color; // Para representar el color asociado a la emoción en UI

    @Column(nullable = false)
    private String icon; // Nombre o URL del icono para la emoción

    @OneToMany(mappedBy = "emotion")
    private List<EmotionRecord> emotionRecords;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}