package com.example.APISkeleton.persistance.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification_logs")
@Getter
@Setter
public class NotificationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String body;

    @Column(nullable = false)
    private String recipientToken; // Token del usuario que recibió la notificación

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // Relación con el usuario

    @Column(nullable = false)
    private LocalDateTime sentAt;
}
