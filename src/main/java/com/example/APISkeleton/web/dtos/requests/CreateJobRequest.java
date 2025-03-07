package com.example.APISkeleton.web.dtos.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateJobRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    private String location;

    @NotNull(message = "Salary is required")
    @Min(value = 0, message = "Salary must be a positive number")
    private Double salary;

    @NotNull(message = "Employer ID is required")
    private Long employerId; // 🔹 Identifica quién publica el trabajo
}
