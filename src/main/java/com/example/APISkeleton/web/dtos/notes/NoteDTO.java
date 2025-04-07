package com.example.APISkeleton.web.dtos.notes;

import com.example.APISkeleton.persistance.entities.Note.NoteType;
import com.example.APISkeleton.web.dtos.emotions.EmotionDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class NoteDTO {
    private Long id;
    private String content;
    private EmotionDTO emotion; // Puede ser null
    private LocalDate noteDate;
    private NoteType type;
    private LocalDateTime createdAt;
}