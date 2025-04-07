package com.example.APISkeleton.web.dtos.registerEmotions;

import com.example.APISkeleton.persistance.entities.EmotionRecord.EmotionIntensity;
import com.example.APISkeleton.web.dtos.emotions.EmotionDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class EmotionRecordDTO {
    private Long id;
    private EmotionDTO emotion;
    private LocalDate recordDate;
    private LocalDateTime recordTime;
    private String note;
    private EmotionIntensity intensity;
}
