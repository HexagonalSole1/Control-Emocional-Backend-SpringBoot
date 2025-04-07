package com.example.APISkeleton.persistance.repositories;

import com.example.APISkeleton.persistance.entities.EmotionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IEmotionRecordRepository extends JpaRepository<EmotionRecord, Long> {

    // Obtener registros de emociones de un usuario por fecha
    List<EmotionRecord> findByUserIdAndRecordDateOrderByRecordTimeDesc(Long userId, LocalDate date);

    // Obtener registros de emociones de un usuario en un rango de fechas
    List<EmotionRecord> findByUserIdAndRecordDateBetweenOrderByRecordDateAscRecordTimeAsc(
            Long userId, LocalDate startDate, LocalDate endDate);

    // Verificar si un usuario ha registrado alguna emoción en una fecha específica
    boolean existsByUserIdAndRecordDate(Long userId, LocalDate date);

    // Contar cuántas veces se ha registrado una emoción en una fecha específica
    @Query("SELECT e.emotion.id, e.emotion.name, COUNT(e) " +
            "FROM EmotionRecord e " +
            "WHERE e.user.id = :userId AND e.recordDate = :date " +
            "GROUP BY e.emotion.id, e.emotion.name")
    List<Object[]> countEmotionsByUserAndDate(Long userId, LocalDate date);

    // Contar emociones por usuario y rango de fechas (para estadísticas semanales)
    @Query("SELECT e.emotion.id, e.emotion.name, e.emotion.color, COUNT(e) " +
            "FROM EmotionRecord e " +
            "WHERE e.user.id = :userId AND e.recordDate BETWEEN :startDate AND :endDate " +
            "GROUP BY e.emotion.id, e.emotion.name, e.emotion.color " +
            "ORDER BY COUNT(e) DESC")
    List<Object[]> countEmotionsByUserAndDateRange(Long userId, LocalDate startDate, LocalDate endDate);

    // Obtener la última emoción registrada por un usuario
    @Query("SELECT e FROM EmotionRecord e WHERE e.user.id = :userId ORDER BY e.recordTime DESC LIMIT 1")
    EmotionRecord findLatestByUserId(Long userId);
}