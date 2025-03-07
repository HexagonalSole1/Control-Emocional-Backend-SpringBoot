package com.example.APISkeleton.web.controllers;

import com.example.APISkeleton.persistance.entities.NotificationLog;
import com.example.APISkeleton.persistance.entities.User;
import com.example.APISkeleton.persistance.repositories.INotificationLogRepository;
import com.example.APISkeleton.persistance.repositories.IUserRepository;
import com.example.APISkeleton.web.dtos.responses.BaseResponse;
import com.example.APISkeleton.web.dtos.responses.NotificationLogDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notificationsLog")
public class NotificationLogController {
    private final INotificationLogRepository notificationLogRepository;
    private final IUserRepository userRepository;

    public NotificationLogController(INotificationLogRepository notificationLogRepository, IUserRepository userRepository) {
        this.notificationLogRepository = notificationLogRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<BaseResponse> getNotificationsByUser(@PathVariable Long userId) {
        List<NotificationLog> notifications = notificationLogRepository.findTopNByUserId(userId,100);

        List<NotificationLogDTO> responseList = notifications.stream().map(notification -> {
            NotificationLogDTO dto = new NotificationLogDTO();
            dto.setId(notification.getId());
            dto.setTitle(notification.getTitle());
            dto.setBody(notification.getBody());
            dto.setSentAt(notification.getSentAt());
            return dto;
        }).toList();

        BaseResponse response = BaseResponse.builder()
                .data(responseList)
                .message("Notifications retrieved successfully")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();

        return response.buildResponseEntity();
    }

    // 🔹 Obtener todas las notificaciones almacenadas en el sistema (opcional)
    @GetMapping
    public ResponseEntity<BaseResponse> getAllNotifications() {
        List<NotificationLog> notifications = notificationLogRepository.findAll();

        BaseResponse response = BaseResponse.builder()
                .data(notifications)
                .message("All notifications retrieved successfully")
                .success(true)
                .httpStatus(org.springframework.http.HttpStatus.OK)
                .build();

        return response.buildResponseEntity();
    }
}
