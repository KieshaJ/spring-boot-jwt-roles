package com.kj.jwt.models;

import com.kj.jwt.utils.enums.RoleEnum;
import com.kj.jwt.utils.models.IdEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "roles")
@Getter
@Setter
@AllArgsConstructor
public class Role extends IdEntity {
    private RoleEnum name;
}
