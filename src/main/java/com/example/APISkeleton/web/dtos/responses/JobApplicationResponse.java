package com.example.APISkeleton.web.dtos.responses;
import com.example.APISkeleton.persistance.entities.enums.ApplicationStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class JobApplicationResponse {
    private Long id;
    private Long jobId;
    private String jobTitle;
    private Long applicantId;
    private String applicantName;
    private ApplicationStatus status;
    private LocalDateTime appliedAt;

    public JobApplicationResponse(Long id, Long jobId, String jobTitle, Long applicantId, String applicantName, ApplicationStatus status, LocalDateTime appliedAt) {
        this.id = id;
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.applicantId = applicantId;
        this.applicantName = applicantName;
        this.status = status;
        this.appliedAt = appliedAt;
    }
}

