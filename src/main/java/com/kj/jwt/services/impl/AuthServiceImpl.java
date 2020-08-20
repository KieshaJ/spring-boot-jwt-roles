package com.kj.jwt.services.impl;

import com.kj.jwt.models.Role;
import com.kj.jwt.models.User;
import com.kj.jwt.repositories.RoleRepository;
import com.kj.jwt.repositories.UserRepository;
import com.kj.jwt.services.AuthService;
import com.kj.jwt.utils.Messages;
import com.kj.jwt.utils.auth.JwtUtils;
import com.kj.jwt.utils.auth.RoleUtils;
import com.kj.jwt.utils.auth.UserDetailsImpl;
import com.kj.jwt.utils.models.TokenUserDetails;
import com.kj.jwt.utils.payloads.requests.LoginRequest;
import com.kj.jwt.utils.payloads.requests.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {
    AuthenticationManager authenticationManager;
    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    JwtUtils jwtUtils;

    @Autowired
    public AuthServiceImpl(
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

    public TokenUserDetails login(LoginRequest request) throws Exception {
        validateLoginRequest(request);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = RoleUtils.roleNamesFromUserDetails(userDetails);

        return new TokenUserDetails(
                jwt,
                userDetails.getId().toString(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles
        );
    }

    public void register(RegisterRequest request) throws Exception {
        validateRegisterRequest(request);

        User user = new User(request.getUsername(), request.getEmail(), passwordEncoder.encode(request.getPassword()));
        Set<String> roleNames = request.getRoles();

        Set<Role> roles = RoleUtils.rolesFromStrings(roleRepository, roleNames);
        user.setRoles(roles);

        userRepository.save(user);
    }

    private void validateLoginRequest(LoginRequest request) throws Exception {
        if(request.getUsername().isBlank() || request.getPassword().isBlank()) {
            throw new Exception(Messages.REQUIRED_FIELDS_NOT_SET);
        }
    }

    private void validateRegisterRequest(RegisterRequest request) throws Exception {
        if(request.getUsername().isBlank() || request.getEmail().isBlank() || request.getPassword().isBlank()) {
            throw new Exception(Messages.REQUIRED_FIELDS_NOT_SET);
        }

        if(userRepository.existsByUsername(request.getUsername())) {
            throw new Exception(Messages.REGISTER_USERNAME_TAKEN);
        }

        if(userRepository.existsByEmail(request.getEmail())) {
            throw new Exception(Messages.REGISTER_EMAIL_TAKEN);
        }
    }
}
