package com.kj.jwt.controllers;

import com.kj.jwt.utils.Messages;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping("/all")
    public ResponseEntity<String> publicContent() {
        return ResponseEntity.ok(Messages.PUBLIC_CONTENT);
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('MOD') or hasRole('ADMIN')")
    public ResponseEntity<String> userContent() {
        return ResponseEntity.ok(Messages.USER_CONTENT);
    }

    @GetMapping("/mod")
    @PreAuthorize("hasRole('MOD') or hasRole('ADMIN')")
    public ResponseEntity<String> modContent() {
        return ResponseEntity.ok(Messages.MOD_CONTENT);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminContent() {
        return ResponseEntity.ok(Messages.ADMIN_CONTENT);
    }
}
