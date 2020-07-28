package com.kj.sessions.utils.payloads.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private ObjectId id;
    private String username;
    private String email;
    private List<String> roles;
}
