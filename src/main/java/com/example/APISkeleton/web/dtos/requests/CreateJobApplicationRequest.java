package com.example.APISkeleton.web.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateJobApplicationRequest {
    @NotNull(message = "Job ID is required")
    private Long jobId;

    @NotNull(message = "Applicant ID is required")
    private Long applicantId;
}
