package com.example.APISkeleton.web.dtos.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticateResponse {
    private String accessToken;
    private String refreshToken;
}