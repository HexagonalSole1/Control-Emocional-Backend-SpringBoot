package com.example.APISkeleton.services.impls;


import com.example.APISkeleton.persistance.entities.NotificationLog;
import com.example.APISkeleton.persistance.entities.User;
import com.example.APISkeleton.persistance.repositories.INotificationLogRepository;
import com.example.APISkeleton.persistance.repositories.IUserRepository;
import com.google.firebase.messaging.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class FirebaseMessagingService {

    private final INotificationLogRepository notificationLogRepository;
    private final IUserRepository userRepository;

    public FirebaseMessagingService(INotificationLogRepository notificationLogRepository, IUserRepository userRepository) {
        this.notificationLogRepository = notificationLogRepository;
        this.userRepository = userRepository;
    }

    public String sendNotification(String title, String body, String token) {
        try {
            // 🔹 Crear el mensaje
            Message message = Message.builder()
                    .setToken(token)
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .build();

            // 🔹 Enviar el mensaje a FCM
            String response = FirebaseMessaging.getInstance().send(message);

            // 🔹 Buscar al usuario con el token (opcional, si se maneja en BD)
            User user = userRepository.findByFCM(token).orElse(null);

            // 🔹 Guardar la notificación en la base de datos
            NotificationLog log = new NotificationLog();
            log.setTitle(title);
            log.setBody(body);
            log.setRecipientToken(token);
            log.setUser(user);  // Se asocia al usuario si existe
            log.setSentAt(LocalDateTime.now());

            notificationLogRepository.save(log);

            return "Successfully sent message: " + response;

        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            return "Error sending FCM message: " + e.getMessage();
        }
    }

    public String sendNotificationToAll(String title, String body) {
        try {
            // 🔹 Crear mensaje dirigido al topic "global"
            Message message = Message.builder()
                    .setTopic("global")  // 📌 Se enviará a todos los suscritos a "global"
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .build();

            // 🔹 Enviar notificación a Firebase
            return FirebaseMessaging.getInstance().send(message);

        } catch (Exception e) {
            return "Error sending FCM message: " + e.getMessage();
        }
    }
}
