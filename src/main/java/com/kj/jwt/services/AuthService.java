package com.kj.jwt.services;

import com.kj.jwt.utils.models.TokenUserDetails;
import com.kj.jwt.utils.payloads.requests.LoginRequest;
import com.kj.jwt.utils.payloads.requests.RegisterRequest;

public interface AuthService {
    TokenUserDetails login(LoginRequest request);
    void register(RegisterRequest request) throws Exception;
}
