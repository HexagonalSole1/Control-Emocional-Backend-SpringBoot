package com.example.APISkeleton.services.impls;


import com.example.APISkeleton.persistance.entities.Emotion;
import com.example.APISkeleton.persistance.entities.EmotionRecord;
import com.example.APISkeleton.persistance.entities.User;
import com.example.APISkeleton.persistance.repositories.IEmotionRecordRepository;
import com.example.APISkeleton.persistance.repositories.IEmotionRepository;
import com.example.APISkeleton.persistance.repositories.IUserRepository;
import com.example.APISkeleton.services.IEmotionRecordService;
import com.example.APISkeleton.web.dtos.emotions.EmotionDTO;
import com.example.APISkeleton.web.dtos.registerEmotions.CreateEmotionRecordRequest;
import com.example.APISkeleton.web.dtos.registerEmotions.EmotionRecordDTO;
import com.example.APISkeleton.web.dtos.registerEmotions.EmotionStatisticsDTO;
import com.example.APISkeleton.web.dtos.responses.BaseResponse;
import com.example.APISkeleton.web.exceptions.AccessDeniedException;
import com.example.APISkeleton.web.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmotionRecordServiceImpl implements IEmotionRecordService {

    private final IEmotionRecordRepository emotionRecordRepository;
    private final IEmotionRepository emotionRepository;
    private final IUserRepository userRepository;

    public EmotionRecordServiceImpl(IEmotionRecordRepository emotionRecordRepository,
                                    IEmotionRepository emotionRepository,
                                    IUserRepository userRepository) {
        this.emotionRecordRepository = emotionRecordRepository;
        this.emotionRepository = emotionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public BaseResponse createEmotionRecord(Long userId, CreateEmotionRecordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(User.class));

        Emotion emotion = emotionRepository.findById(request.getEmotionId())
                .orElseThrow(() -> new ResourceNotFoundException(Emotion.class));

        EmotionRecord emotionRecord = new EmotionRecord();
        emotionRecord.setUser(user);
        emotionRecord.setEmotion(emotion);
        emotionRecord.setRecordDate(LocalDate.now());
        emotionRecord.setRecordTime(LocalDateTime.now());
        emotionRecord.setNote(request.getNote());
        emotionRecord.setIntensity(request.getIntensity());

        emotionRecord = emotionRecordRepository.save(emotionRecord);

        return BaseResponse.builder()
                .data(mapToDTO(emotionRecord))
                .message("Registro de emoción creado con éxito")
                .success(true)
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    @Override
    public BaseResponse getEmotionRecordsByDate(Long userId, LocalDate date) {
        List<EmotionRecord> records = emotionRecordRepository.findByUserIdAndRecordDateOrderByRecordTimeDesc(userId, date);

        List<EmotionRecordDTO> recordDTOs = records.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return BaseResponse.builder()
                .data(recordDTOs)
                .message("Registros de emociones obtenidos con éxito")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @Override
    public BaseResponse getEmotionRecordsByDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        List<EmotionRecord> records = emotionRecordRepository.findByUserIdAndRecordDateBetweenOrderByRecordDateAscRecordTimeAsc(
                userId, startDate, endDate);

        List<EmotionRecordDTO> recordDTOs = records.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return BaseResponse.builder()
                .data(recordDTOs)
                .message("Registros de emociones obtenidos con éxito")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @Override
    public BaseResponse getEmotionStatisticsByWeek(Long userId, LocalDate weekStartDate) {
        // Si no se proporciona una fecha de inicio de semana, usar la semana actual
        if (weekStartDate == null) {
            weekStartDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        } else {
            // Ajustar a lunes si no lo es
            weekStartDate = weekStartDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        }

        LocalDate weekEndDate = weekStartDate.plusDays(6); // Domingo

        // Obtener estadísticas por rango de fechas
        List<Object[]> emotionCounts = emotionRecordRepository.countEmotionsByUserAndDateRange(
                userId, weekStartDate, weekEndDate);

        // Obtener registros de emociones por día
        List<EmotionRecord> records = emotionRecordRepository.findByUserIdAndRecordDateBetweenOrderByRecordDateAscRecordTimeAsc(
                userId, weekStartDate, weekEndDate);

        EmotionStatisticsDTO statistics = new EmotionStatisticsDTO();
        Map<String, Long> countMap = new HashMap<>();
        Map<String, String> colorMap = new HashMap<>();

        // Procesar conteos generales de emociones
        for (Object[] result : emotionCounts) {
            String emotionName = (String) result[1];
            String emotionColor = (String) result[2];
            Long count = (Long) result[3];

            countMap.put(emotionName, count);
            colorMap.put(emotionName, emotionColor);
        }

        statistics.setEmotionCounts(countMap);
        statistics.setEmotionColors(colorMap);

        // Agrupar registros por día
        Map<LocalDate, Map<String, Long>> dailyEmotionsMap = new HashMap<>();

        // Inicializar el mapa para todos los días de la semana
        for (int i = 0; i <= 6; i++) {
            LocalDate date = weekStartDate.plusDays(i);
            dailyEmotionsMap.put(date, new HashMap<>());
        }

        // Agrupar registros por día y contar ocurrencias de emociones
        for (EmotionRecord record : records) {
            Map<String, Long> dayEmotions = dailyEmotionsMap.get(record.getRecordDate());
            String emotionName = record.getEmotion().getName();

            dayEmotions.put(emotionName, dayEmotions.getOrDefault(emotionName, 0L) + 1);
        }

        // Convertir mapa a lista de DailyEmotionDTO
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<EmotionStatisticsDTO.DailyEmotionDTO> dailyEmotions = new ArrayList<>();

        for (Map.Entry<LocalDate, Map<String, Long>> entry : dailyEmotionsMap.entrySet()) {
            EmotionStatisticsDTO.DailyEmotionDTO dailyEmotion = new EmotionStatisticsDTO.DailyEmotionDTO();
            dailyEmotion.setDate(entry.getKey().format(formatter));
            dailyEmotion.setEmotions(entry.getValue());
            dailyEmotions.add(dailyEmotion);
        }

        // Ordenar por fecha
        dailyEmotions.sort(Comparator.comparing(EmotionStatisticsDTO.DailyEmotionDTO::getDate));

        statistics.setDailyEmotions(dailyEmotions);

        return BaseResponse.builder()
                .data(statistics)
                .message("Estadísticas de emociones obtenidas con éxito")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @Override
    public BaseResponse deleteEmotionRecord(Long userId, Long recordId) {
        EmotionRecord record = emotionRecordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException(EmotionRecord.class));

        // Verificar que el registro pertenezca al usuario
        if (!record.getUser().getId().equals(userId)) {
            throw new AccessDeniedException();
        }

        emotionRecordRepository.delete(record);

        return BaseResponse.builder()
                .message("Registro de emoción eliminado con éxito")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @Override
    public BaseResponse checkDailyEmotionRecordStatus(Long userId) {
        LocalDate today = LocalDate.now();
        boolean hasRecordedEmotion = emotionRecordRepository.existsByUserIdAndRecordDate(userId, today);

        Map<String, Object> response = new HashMap<>();
        response.put("hasRecordedEmotion", hasRecordedEmotion);
        response.put("date", today);

        return BaseResponse.builder()
                .data(response)
                .message("Estado de registro de emoción obtenido con éxito")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    // Método auxiliar para mapear un EmotionRecord a un EmotionRecordDTO
    private EmotionRecordDTO mapToDTO(EmotionRecord record) {
        EmotionRecordDTO dto = new EmotionRecordDTO();
        dto.setId(record.getId());
        dto.setRecordDate(record.getRecordDate());
        dto.setRecordTime(record.getRecordTime());
        dto.setNote(record.getNote());
        dto.setIntensity(record.getIntensity());

        // Mapear la emoción
        EmotionDTO emotionDTO = new EmotionDTO();
        emotionDTO.setId(record.getEmotion().getId());
        emotionDTO.setName(record.getEmotion().getName());
        emotionDTO.setDescription(record.getEmotion().getDescription());
        emotionDTO.setColor(record.getEmotion().getColor());
        emotionDTO.setIcon(record.getEmotion().getIcon());

        dto.setEmotion(emotionDTO);

        return dto;
    }
}