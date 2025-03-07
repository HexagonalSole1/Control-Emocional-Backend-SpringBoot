package com.example.APISkeleton.services;

import com.example.APISkeleton.web.dtos.requests.AuthenticateRequest;
import com.example.APISkeleton.web.dtos.requests.RefreshTokenRequest;
import com.example.APISkeleton.web.dtos.responses.BaseResponse;

public interface IAuthService {
    BaseResponse authenticate(AuthenticateRequest request);
    BaseResponse refreshToken(RefreshTokenRequest request);
}
