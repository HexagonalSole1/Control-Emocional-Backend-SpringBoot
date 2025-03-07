package com.example.APISkeleton.web.dtos.responses;

import com.example.APISkeleton.persistance.entities.enums.ApplicationStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class JobApplicationResponseDTO {
    private Long id;
    private Long applicantId;
    private String applicantEmail;  // Solo el email del usuario
    private Long jobId;
    private String jobTitle;  // Solo el título del trabajo
    private ApplicationStatus status;
    private LocalDateTime appliedAt;

    public JobApplicationResponseDTO(Long id, Long applicantId, String applicantEmail,
                                     Long jobId, String jobTitle, ApplicationStatus status, LocalDateTime appliedAt) {
        this.id = id;
        this.applicantId = applicantId;
        this.applicantEmail = applicantEmail;
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.status = status;
        this.appliedAt = appliedAt;
    }
}
