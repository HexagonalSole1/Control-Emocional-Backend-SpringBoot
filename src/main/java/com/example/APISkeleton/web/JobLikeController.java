package com.example.APISkeleton.web.controllers;

import com.example.APISkeleton.services.IJobService;
import com.example.APISkeleton.web.dtos.responses.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobs/likes")
public class JobLikeController {
    private final IJobService jobService;

    public JobLikeController(IJobService jobService) {
        this.jobService = jobService;
    }

    // 🔹 Dar like a un trabajo
    @PostMapping("/{jobId}/user/{userId}")
    public ResponseEntity<BaseResponse> likeJob(@PathVariable Long jobId, @PathVariable Long userId) {
        jobService.likeJob(jobId, userId);
        BaseResponse response = BaseResponse.builder()
                .message("Job liked successfully")
                .success(true)
                .httpStatus(org.springframework.http.HttpStatus.OK)
                .build();
        return response.buildResponseEntity();
    }

    // 🔹 Quitar like a un trabajo
    @DeleteMapping("/{jobId}/user/{userId}")
    public ResponseEntity<BaseResponse> unlikeJob(@PathVariable Long jobId, @PathVariable Long userId) {
        jobService.unlikeJob(jobId, userId);
        BaseResponse response = BaseResponse.builder()
                .message("Job unliked successfully")
                .success(true)
                .httpStatus(org.springframework.http.HttpStatus.OK)
                .build();
        return response.buildResponseEntity();
    }
}
