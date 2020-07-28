package com.kj.sessions.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class MainController {
    @GetMapping("/")
    public ResponseEntity<String> adminGreeting(HttpSession session) {
        return ResponseEntity.ok("Hello, Admin!");
    }
}
