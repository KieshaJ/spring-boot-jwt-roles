package com.kj.jwt.repositories;

import com.kj.jwt.models.Role;
import com.kj.jwt.utils.enums.RoleEnum;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, ObjectId> {
    Optional<Role> findByName(RoleEnum name);
}
