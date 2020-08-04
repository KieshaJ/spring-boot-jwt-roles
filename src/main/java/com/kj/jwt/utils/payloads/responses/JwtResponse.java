package com.kj.jwt.utils.payloads.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class JwtResponse {
    private final String token;
    private final String id;
    private final String username;
    private final String email;
    private final List<String> roles;
}
