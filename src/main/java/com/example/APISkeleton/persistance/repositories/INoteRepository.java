package com.example.APISkeleton.persistance.repositories;

import com.example.APISkeleton.persistance.entities.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface INoteRepository extends JpaRepository<Note, Long> {

    // Obtener notas de un usuario por fecha
    List<Note> findByUserIdAndNoteDateOrderByCreatedAtDesc(Long userId, LocalDate date);

    // Obtener notas de un usuario en un rango de fechas
    List<Note> findByUserIdAndNoteDateBetweenOrderByNoteDateDescCreatedAtDesc(
            Long userId, LocalDate startDate, LocalDate endDate);

    // Obtener notas de un usuario por tipo y fecha
    List<Note> findByUserIdAndTypeAndNoteDateOrderByCreatedAtDesc(
            Long userId, Note.NoteType type, LocalDate date);

    // Verificar si un usuario ha escrito alguna nota en una fecha específica
    boolean existsByUserIdAndNoteDate(Long userId, LocalDate date);

    // Contar notas por tipo en un rango de fechas
    @Query("SELECT n.type, COUNT(n) FROM Note n " +
            "WHERE n.user.id = :userId AND n.noteDate BETWEEN :startDate AND :endDate " +
            "GROUP BY n.type")
    List<Object[]> countNotesByTypeAndDateRange(Long userId, LocalDate startDate, LocalDate endDate);

    // Obtener la última nota de un usuario
    @Query("SELECT n FROM Note n WHERE n.user.id = :userId ORDER BY n.createdAt DESC LIMIT 1")
    Note findLatestByUserId(Long userId);
}