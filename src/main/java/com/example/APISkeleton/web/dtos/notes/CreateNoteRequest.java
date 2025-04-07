package com.example.APISkeleton.web.dtos.notes;

import com.example.APISkeleton.persistance.entities.Note.NoteType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateNoteRequest {
    @NotBlank(message = "El contenido de la nota es obligatorio")
    private String content;

    private Long emotionId; // ID de la emoción asociada (opcional)

    @NotNull(message = "El tipo de nota es obligatorio")
    private NoteType type = NoteType.GENERAL;
}