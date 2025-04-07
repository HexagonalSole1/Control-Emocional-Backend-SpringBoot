package com.example.APISkeleton.web.dtos.emotions;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateEmotionRequest {
    @NotBlank(message = "El nombre de la emoción es obligatorio")
    private String name;

    private String description;

    @NotBlank(message = "El color es obligatorio")
    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = "Formato de color inválido. Debe ser un código hexadecimal (ej: #FF5733)")
    private String color;

    @NotBlank(message = "El ícono es obligatorio")
    private String icon;
}