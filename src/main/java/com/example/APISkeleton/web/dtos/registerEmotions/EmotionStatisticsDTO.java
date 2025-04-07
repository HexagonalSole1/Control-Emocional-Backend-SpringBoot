package com.example.APISkeleton.web.dtos.registerEmotions;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class EmotionStatisticsDTO {
    private Map<String, Long> emotionCounts; // Mapa de nombre de emoción a conteo
    private Map<String, String> emotionColors; // Mapa de nombre de emoción a color
    private List<DailyEmotionDTO> dailyEmotions; // Emociones por día

    @Getter
    @Setter
    public static class DailyEmotionDTO {
        private String date; // Formato: "YYYY-MM-DD"
        private Map<String, Long> emotions; // Mapa de nombre de emoción a conteo
    }
}