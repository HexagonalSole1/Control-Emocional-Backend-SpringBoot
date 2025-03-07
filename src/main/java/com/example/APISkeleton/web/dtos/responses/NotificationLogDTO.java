package com.example.APISkeleton.web.dtos.responses;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class NotificationLogDTO {
    private Long id;
    private String title;
    private String body;
    private LocalDateTime sentAt;
}
