package com.example.APISkeleton.web.dtos.registerEmotions;

import com.example.APISkeleton.persistance.entities.EmotionRecord;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateEmotionRecordRequest {
    @NotNull(message = "El ID de la emoción es obligatorio")
    private Long emotionId;

    private String note;

    private EmotionRecord.EmotionIntensity intensity = EmotionRecord.EmotionIntensity.MEDIUM;
}
