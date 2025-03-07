package com.example.APISkeleton.services;

import com.example.APISkeleton.persistance.entities.JobApplication;
import com.example.APISkeleton.persistance.entities.enums.ApplicationStatus;
import com.example.APISkeleton.web.dtos.requests.CreateJobApplicationRequest;
import com.example.APISkeleton.web.dtos.requests.UpdateApplicationStatusRequest;
import com.example.APISkeleton.web.dtos.responses.BaseResponse;

import java.util.List;

public interface IJobApplicationService {
    BaseResponse updateApplicationStatus(UpdateApplicationStatusRequest request);

    BaseResponse applyForJob(CreateJobApplicationRequest request);
    BaseResponse getApplicationsByJob(Long jobId);
    BaseResponse getApplicationsByUser(Long userId);
    BaseResponse withdrawApplication(Long applicationId);
    BaseResponse getApplicationsByUserAndStatus(Long userId, ApplicationStatus status);

}
