package com.kj.jwt.models;

import com.kj.jwt.utils.models.IdEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Document(collection = "users")
@Getter
@Setter
public class User extends IdEntity {
    private String username;
    private String email;
    private String password;
    private Set<Role> roles = new HashSet<>();

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
