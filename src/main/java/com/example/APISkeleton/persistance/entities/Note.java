package com.example.APISkeleton.persistance.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "notes")
@Getter
@Setter
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "emotion_id")
    private Emotion emotion; // Emoción asociada (opcional)

    @Column(nullable = false)
    private LocalDate noteDate; // Fecha de la nota

    @Enumerated(EnumType.STRING)
    private NoteType type; // Tipo de nota

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Enum para los tipos de notas
    public enum NoteType {
        GENERAL,
        SITUATION,
        THOUGHT,
        EMOTION
    }
}