package com.example.APISkeleton.persistance.repositories;

import com.example.APISkeleton.persistance.entities.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface INotificationLogRepository extends JpaRepository<NotificationLog, Long> {

    // 🔹 Obtener notificaciones de un usuario ordenadas por fecha de envío (más recientes primero)
    List<NotificationLog> findByUserIdOrderBySentAtDesc(Long userId);

    // 🔹 Obtener las últimas N notificaciones de un usuario
    @Query("SELECT n FROM NotificationLog n WHERE n.user.id = :userId ORDER BY n.sentAt DESC LIMIT :limit")
    List<NotificationLog> findTopNByUserId(Long userId, int limit);
}
