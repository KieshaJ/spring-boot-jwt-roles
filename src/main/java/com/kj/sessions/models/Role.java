package com.kj.sessions.models;

import com.kj.sessions.utils.enums.RoleEnum;
import com.kj.sessions.utils.models.IdEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "roles")
@Getter
@Setter
public class Role extends IdEntity {
    private RoleEnum name;

    public Role() {}

    public Role(RoleEnum name) {
        this.name = name;
    }
}
