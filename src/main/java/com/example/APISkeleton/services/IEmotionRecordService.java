package com.example.APISkeleton.services;

import com.example.APISkeleton.web.dtos.registerEmotions.CreateEmotionRecordRequest;
import com.example.APISkeleton.web.dtos.responses.BaseResponse;
import java.time.LocalDate;

public interface IEmotionRecordService {
    BaseResponse createEmotionRecord(Long userId, CreateEmotionRecordRequest request);
    BaseResponse getEmotionRecordsByDate(Long userId, LocalDate date);
    BaseResponse getEmotionRecordsByDateRange(Long userId, LocalDate startDate, LocalDate endDate);
    BaseResponse getEmotionStatisticsByWeek(Long userId, LocalDate weekStartDate);
    BaseResponse deleteEmotionRecord(Long userId, Long recordId);
    BaseResponse checkDailyEmotionRecordStatus(Long userId);
}
