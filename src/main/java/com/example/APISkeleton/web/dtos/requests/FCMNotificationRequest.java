package com.example.APISkeleton.web.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FCMNotificationRequest {
    @NotBlank
    private String token;

    @NotBlank
    private String title;

    @NotBlank
    private String body;
}
