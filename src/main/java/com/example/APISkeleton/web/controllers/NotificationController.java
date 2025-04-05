package com.example.APISkeleton.web.controllers;

import com.example.APISkeleton.services.impls.FirebaseMessagingService;
import com.example.APISkeleton.web.dtos.requests.FCMNotificationRequest;
import com.example.APISkeleton.web.dtos.responses.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final FirebaseMessagingService firebaseMessagingService;

    public NotificationController(FirebaseMessagingService firebaseMessagingService) {
        this.firebaseMessagingService = firebaseMessagingService;
    }

    @PostMapping("/send")
    public ResponseEntity<BaseResponse> sendNotification(@RequestBody FCMNotificationRequest request) {
        String result = firebaseMessagingService.sendNotification(request.getTitle(), request.getBody(), request.getToken());

        BaseResponse response = BaseResponse.builder()
                .data(result)
                .message("Notification processed")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();

        return response.buildResponseEntity();
    }
}
