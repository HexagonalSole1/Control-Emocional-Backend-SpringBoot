package com.example.APISkeleton.web.controllers;

import com.example.APISkeleton.services.IEmotionService;
import com.example.APISkeleton.web.dtos.emotions.CreateEmotionRequest;
import com.example.APISkeleton.web.dtos.responses.BaseResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emotions")
public class EmotionController {

    private final IEmotionService emotionService;

    public EmotionController(IEmotionService emotionService) {
        this.emotionService = emotionService;
    }

    @GetMapping
    public ResponseEntity<BaseResponse> getAllEmotions() {
        BaseResponse response = emotionService.getAllEmotions();
        return response.buildResponseEntity();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getEmotionById(@PathVariable Long id) {
        BaseResponse response = emotionService.getEmotionById(id);
        return response.buildResponseEntity();
    }

    @PostMapping
    public ResponseEntity<BaseResponse> createEmotion(@Valid @RequestBody CreateEmotionRequest request) {
        BaseResponse response = emotionService.createEmotion(request);
        return response.buildResponseEntity();
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updateEmotion(@PathVariable Long id,
                                                      @Valid @RequestBody CreateEmotionRequest request) {
        BaseResponse response = emotionService.updateEmotion(id, request);
        return response.buildResponseEntity();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteEmotion(@PathVariable Long id) {
        BaseResponse response = emotionService.deleteEmotion(id);
        return response.buildResponseEntity();
    }
}