package com.kj.jwt.utils.models;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

@Getter
@Setter
public class IdEntity {
    @Id
    private ObjectId id;
}
