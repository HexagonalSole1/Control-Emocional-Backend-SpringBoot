package com.example.APISkeleton.services;

import com.example.APISkeleton.persistance.entities.Emotion;
import com.example.APISkeleton.web.dtos.emotions.CreateEmotionRequest;
import com.example.APISkeleton.web.dtos.responses.BaseResponse;

public interface IEmotionService {
    BaseResponse getAllEmotions();
    BaseResponse getEmotionById(Long id);
    BaseResponse createEmotion(CreateEmotionRequest request);
    BaseResponse updateEmotion(Long id, CreateEmotionRequest request);
    BaseResponse deleteEmotion(Long id);
}
