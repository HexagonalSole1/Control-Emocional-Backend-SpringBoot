package com.example.APISkeleton.web.controllers;

import com.example.APISkeleton.security.entities.UseDetailsImpl;
import com.example.APISkeleton.services.IEmotionRecordService;
import com.example.APISkeleton.web.dtos.registerEmotions.CreateEmotionRecordRequest;
import com.example.APISkeleton.web.dtos.responses.BaseResponse;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/emotion-records")
public class EmotionRecordController {

    private final IEmotionRecordService emotionRecordService;

    public EmotionRecordController(IEmotionRecordService emotionRecordService) {
        this.emotionRecordService = emotionRecordService;
    }

    @PostMapping
    public ResponseEntity<BaseResponse> createEmotionRecord(
            @AuthenticationPrincipal UseDetailsImpl userDetails,
            @Valid @RequestBody CreateEmotionRecordRequest request) {
        Long userId = userDetails.getUser().getId();
        BaseResponse response = emotionRecordService.createEmotionRecord(userId, request);
        return response.buildResponseEntity();
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<BaseResponse> getEmotionRecordsByDate(
            @AuthenticationPrincipal UseDetailsImpl userDetails,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Long userId = userDetails.getUser().getId();
        BaseResponse response = emotionRecordService.getEmotionRecordsByDate(userId, date);
        return response.buildResponseEntity();
    }

    @GetMapping("/today")
    public ResponseEntity<BaseResponse> getTodayEmotionRecords(
            @AuthenticationPrincipal UseDetailsImpl userDetails) {
        Long userId = userDetails.getUser().getId();
        LocalDate today = LocalDate.now();
        BaseResponse response = emotionRecordService.getEmotionRecordsByDate(userId, today);
        return response.buildResponseEntity();
    }

    @GetMapping("/range")
    public ResponseEntity<BaseResponse> getEmotionRecordsByDateRange(
            @AuthenticationPrincipal UseDetailsImpl userDetails,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Long userId = userDetails.getUser().getId();
        BaseResponse response = emotionRecordService.getEmotionRecordsByDateRange(userId, startDate, endDate);
        return response.buildResponseEntity();
    }

    @GetMapping("/statistics/week")
    public ResponseEntity<BaseResponse> getWeeklyStatistics(
            @AuthenticationPrincipal UseDetailsImpl userDetails,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart) {
        Long userId = userDetails.getUser().getId();
        BaseResponse response = emotionRecordService.getEmotionStatisticsByWeek(userId, weekStart);
        return response.buildResponseEntity();
    }

    @DeleteMapping("/{recordId}")
    public ResponseEntity<BaseResponse> deleteEmotionRecord(
            @AuthenticationPrincipal UseDetailsImpl userDetails,
            @PathVariable Long recordId) {
        Long userId = userDetails.getUser().getId();
        BaseResponse response = emotionRecordService.deleteEmotionRecord(userId, recordId);
        return response.buildResponseEntity();
    }

    @GetMapping("/status")
    public ResponseEntity<BaseResponse> checkDailyEmotionRecordStatus(
            @AuthenticationPrincipal UseDetailsImpl userDetails) {
        Long userId = userDetails.getUser().getId();
        BaseResponse response = emotionRecordService.checkDailyEmotionRecordStatus(userId);
        return response.buildResponseEntity();
    }
}