package com.example.APISkeleton.web.dtos.requests;

import com.example.APISkeleton.persistance.entities.enums.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateApplicationStatusRequest {

    @NotNull(message = "El ID de la aplicación es obligatorio")
    private Long applicationId;

    @NotNull(message = "El estado de la aplicación es obligatorio")
    private ApplicationStatus status;
}
