package com.kj.jwt.controllers;

import com.kj.jwt.services.AuthService;
import com.kj.jwt.utils.Messages;
import com.kj.jwt.utils.models.TokenUserDetails;
import com.kj.jwt.utils.payloads.requests.LoginRequest;
import com.kj.jwt.utils.payloads.requests.RegisterRequest;
import com.kj.jwt.utils.payloads.responses.MessageAndBodyResponse;
import com.kj.jwt.utils.payloads.responses.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<MessageResponse> login (@RequestBody LoginRequest loginRequest) {
        try {
            TokenUserDetails tokenUserDetails = authService.login(loginRequest);

            return ResponseEntity.ok(
                    new MessageAndBodyResponse(
                            Messages.LOGIN_SUCCESS,
                            tokenUserDetails
                    )
            );
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register (@RequestBody RegisterRequest registerRequest) {
        try {
            authService.register(registerRequest);
            return ResponseEntity.ok(new MessageResponse(Messages.REGISTER_SUCCESS));
        }
        catch(Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse(e.getMessage()));
        }
    }
}
