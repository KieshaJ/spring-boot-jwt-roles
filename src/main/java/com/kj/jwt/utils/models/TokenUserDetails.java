package com.kj.jwt.utils.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TokenUserDetails {
    private final String jwt;
    private final String id;
    private final String username;
    private final String email;
    private final List<String> roles;
}
