package com.kj.sessions.utils.payloads.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
public class LoginRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
