package com.kj.sessions.controllers;

import com.kj.sessions.models.Role;
import com.kj.sessions.models.User;
import com.kj.sessions.repositories.RoleRepository;
import com.kj.sessions.repositories.UserRepository;
import com.kj.sessions.utils.auth.JwtUtils;
import com.kj.sessions.utils.auth.UserDetailsImpl;
import com.kj.sessions.utils.enums.RoleEnum;
import com.kj.sessions.utils.payloads.requests.LoginRequest;
import com.kj.sessions.utils.payloads.requests.RegisterRequest;
import com.kj.sessions.utils.payloads.responses.JwtResponse;
import com.kj.sessions.utils.payloads.responses.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    AuthenticationManager authenticationManager;
    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    JwtUtils jwtUtils;

    @Autowired
    public AuthController(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            JwtUtils jwtUtils
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login (@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles)
        );
    }

    @PostMapping("/register")
    public ResponseEntity<?> register (@Valid @RequestBody RegisterRequest registerRequest) {
        if(userRepository.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already in use!"));
        }

        if(userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        User user = new User(registerRequest.getUsername(), registerRequest.getEmail(), registerRequest.getPassword());
        Set<String> roleNames = registerRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if(roleNames == null) {
            Role userRole = roleRepository.findByName(RoleEnum.USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role not found!"));
            roles.add(userRole);
        }
        else {
            roleNames.forEach(roleName -> {
                switch (roleName) {
                    case "ADMIN" -> {
                        Role adminRole = roleRepository.findByName(RoleEnum.ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role not found!"));
                        roles.add(adminRole);
                    }
                    case "MOD" -> {
                        Role modRole = roleRepository.findByName(RoleEnum.MOD)
                                .orElseThrow(() -> new RuntimeException("Error: Role not found!"));
                        roles.add(modRole);
                    }
                    default -> {
                        Role userRole = roleRepository.findByName(RoleEnum.USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role not found!"));
                        roles.add(userRole);
                    }
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
