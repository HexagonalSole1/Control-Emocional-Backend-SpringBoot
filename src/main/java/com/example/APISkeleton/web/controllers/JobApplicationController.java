package com.example.APISkeleton.web.controllers;

import com.example.APISkeleton.persistance.entities.enums.ApplicationStatus;
import com.example.APISkeleton.services.IJobApplicationService;
import com.example.APISkeleton.web.dtos.requests.CreateJobApplicationRequest;
import com.example.APISkeleton.web.dtos.requests.UpdateApplicationStatusRequest;
import com.example.APISkeleton.web.dtos.responses.BaseResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/job-applications")
public class JobApplicationController {

    private final IJobApplicationService jobApplicationService;

    public JobApplicationController(IJobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }
    // 🔹 Endpoint para actualizar el estado de una postulación
    @PatchMapping("/status")
    public ResponseEntity<BaseResponse> updateApplicationStatus(
            @Valid @RequestBody UpdateApplicationStatusRequest request) {
        BaseResponse response = jobApplicationService.updateApplicationStatus(request);
        return response.buildResponseEntity();
    }
    @PostMapping
    public ResponseEntity<BaseResponse> applyForJob(@Valid @RequestBody CreateJobApplicationRequest request) {
        return jobApplicationService.applyForJob(request).buildResponseEntity();
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<BaseResponse> getApplicationsByJob(@PathVariable Long jobId) {
        return jobApplicationService.getApplicationsByJob(jobId).buildResponseEntity();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<BaseResponse> getApplicationsByUser(@PathVariable Long userId) {
        return jobApplicationService.getApplicationsByUser(userId).buildResponseEntity();
    }


    @DeleteMapping("/{applicationId}")
    public ResponseEntity<BaseResponse> withdrawApplication(@PathVariable Long applicationId) {
        return jobApplicationService.withdrawApplication(applicationId).buildResponseEntity();
    }
    @GetMapping("/user/{userId}/accepted")
    public ResponseEntity<BaseResponse> getAcceptedApplicationsByUser(@PathVariable Long userId) {
        return jobApplicationService.getApplicationsByUserAndStatus(userId, ApplicationStatus.ACCEPTED).buildResponseEntity();
    }

    // 🔹 Obtener postulaciones de un usuario con estado PENDIENTE
    @GetMapping("/user/{userId}/pending")
    public ResponseEntity<BaseResponse> getPendingApplicationsByUser(@PathVariable Long userId) {
        return jobApplicationService.getApplicationsByUserAndStatus(userId, ApplicationStatus.PENDING).buildResponseEntity();
    }

}
