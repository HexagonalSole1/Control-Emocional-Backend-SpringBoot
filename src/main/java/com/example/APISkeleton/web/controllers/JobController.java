package com.example.APISkeleton.web.controllers;

import com.example.APISkeleton.services.IJobService;
import com.example.APISkeleton.web.dtos.requests.CreateJobRequest;
import com.example.APISkeleton.web.dtos.responses.BaseResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobs")
public class JobController {
    private final IJobService jobService;

    public JobController(IJobService jobService) {
        this.jobService = jobService;
    }

    // 🔹 Obtener un trabajo por ID con BaseResponse
    @GetMapping("/{jobId}")
    public ResponseEntity<BaseResponse> getJobById(@PathVariable Long jobId) {
        BaseResponse baseResponse = jobService.getById(jobId);
        return baseResponse.buildResponseEntity();
    }

    // 🔹 Obtener un trabajo por ID con BaseResponse
    @GetMapping("/user/{userId}")
    public ResponseEntity<BaseResponse> getJobByIdUserNotApplied(@PathVariable Long userId) {
        BaseResponse baseResponse = jobService.getAllByIdUser(userId);
        return baseResponse.buildResponseEntity();
    }

    // 🔹 Obtener un trabajo por ID con BaseResponse
    @GetMapping("/userApplied/{userId}")
    public ResponseEntity<BaseResponse> getJobByIdUserApplied(@PathVariable Long userId) {
        BaseResponse baseResponse = jobService.getAllByIdUserApplied(userId);
        return baseResponse.buildResponseEntity();
    }

    // 🔹 Obtener todos los trabajos con BaseResponse
    @GetMapping
    public ResponseEntity<BaseResponse> getAllJobs() {
        BaseResponse baseResponse = jobService.getAll();
        return baseResponse.buildResponseEntity();
    }

    // 🔹 Crear un nuevo trabajo con validación
    @PostMapping
    public ResponseEntity<BaseResponse> createJob(@Valid @RequestBody CreateJobRequest request) {
        BaseResponse baseResponse = jobService.createJob(request);
        return baseResponse.buildResponseEntity();
    }
}
