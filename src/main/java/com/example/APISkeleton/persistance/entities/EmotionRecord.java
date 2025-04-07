package com.example.APISkeleton.persistance.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "emotion_records")
@Getter
@Setter
public class EmotionRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "emotion_id", nullable = false)
    private Emotion emotion;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate recordDate; // Fecha del registro (sin hora)

    @Column(nullable = false)
    private LocalDateTime recordTime; // Fecha y hora del registro

    private String note; // Nota opcional asociada a este registro de emoción

    @Enumerated(EnumType.STRING)
    private EmotionIntensity intensity; // Intensidad de la emoción

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Enum para la intensidad de la emoción
    public enum EmotionIntensity {
        LOW, MEDIUM, HIGH
    }
}