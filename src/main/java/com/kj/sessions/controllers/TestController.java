package com.kj.sessions.controllers;

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
        return ResponseEntity.ok("Public content.");
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('MOD') or hasRole('ADMIN')")
    public ResponseEntity<String> userContent() {
        return ResponseEntity.ok("User content.");
    }

    @GetMapping("/mod")
    @PreAuthorize("hasRole('MOD')")
    public ResponseEntity<String> modContent() {
        return ResponseEntity.ok("Mod content.");
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminContent() {
        return ResponseEntity.ok("Admin content.");
    }
}
