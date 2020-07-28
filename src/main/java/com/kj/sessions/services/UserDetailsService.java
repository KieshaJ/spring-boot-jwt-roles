package com.kj.sessions.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserDetailsService {
    UserDetails findByUsername(String username) throws UsernameNotFoundException;
}
