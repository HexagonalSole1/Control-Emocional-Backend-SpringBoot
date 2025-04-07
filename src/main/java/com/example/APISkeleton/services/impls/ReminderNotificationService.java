package com.example.APISkeleton.services.impls;

import com.example.APISkeleton.persistance.entities.User;
import com.example.APISkeleton.persistance.repositories.IEmotionRecordRepository;
import com.example.APISkeleton.persistance.repositories.INoteRepository;
import com.example.APISkeleton.persistance.repositories.IUserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReminderNotificationService {

    private final IUserRepository userRepository;
    private final IEmotionRecordRepository emotionRecordRepository;
    private final INoteRepository noteRepository;
    private final FirebaseMessagingService firebaseMessagingService;

    public ReminderNotificationService(IUserRepository userRepository,
                                       IEmotionRecordRepository emotionRecordRepository,
                                       INoteRepository noteRepository,
                                       FirebaseMessagingService firebaseMessagingService) {
        this.userRepository = userRepository;
        this.emotionRecordRepository = emotionRecordRepository;
        this.noteRepository = noteRepository;
        this.firebaseMessagingService = firebaseMessagingService;
    }

    /**
     * Envía notificaciones a los usuarios que no han registrado emociones hoy.
     * Se ejecuta todos los días a las 8:00 PM.
     */
    @Scheduled(cron = "0 0 20 * * *") // Todos los días a las 8:00 PM
    public void sendEmotionReminders() {
        LocalDate today = LocalDate.now();

        // Obtener todos los usuarios con un token FCM configurado
        List<User> users = userRepository.findAll().stream()
                .filter(user -> user.getFCM() != null && !user.getFCM().isEmpty())
                .toList();

        for (User user : users) {
            boolean hasRecordedEmotion = emotionRecordRepository.existsByUserIdAndRecordDate(user.getId(), today);

            if (!hasRecordedEmotion) {
                String title = "¡Registra tu emoción!";
                String body = "No has registrado cómo te sientes hoy. Toma un momento para reflexionar y registrarlo.";

                firebaseMessagingService.sendNotification(title, body, user.getFCM());
            }
        }
    }

    /**
     * Envía notificaciones a los usuarios que no han escrito una nota hoy.
     * Se ejecuta todos los días a las 8:00 PM.
     */
    @Scheduled(cron = "0 0 20 * * *") // Todos los días a las 8:00 PM
    public void sendNoteReminders() {
        LocalDate today = LocalDate.now();

        // Obtener todos los usuarios con un token FCM configurado
        List<User> users = userRepository.findAll().stream()
                .filter(user -> user.getFCM() != null && !user.getFCM().isEmpty())
                .toList();

        for (User user : users) {
            boolean hasCreatedNote = noteRepository.existsByUserIdAndNoteDate(user.getId(), today);

            if (!hasCreatedNote) {
                String title = "¡Escribe en tu diario!";
                String body = "No has escrito ninguna nota hoy. Dedica un momento para reflexionar sobre tu día.";

                firebaseMessagingService.sendNotification(title, body, user.getFCM());
            }
        }
    }
}