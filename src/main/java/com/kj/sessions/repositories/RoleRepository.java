package com.kj.sessions.repositories;

import com.kj.sessions.models.Role;
import com.kj.sessions.utils.enums.RoleEnum;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, ObjectId> {
    Optional<Role> findByName(RoleEnum name);
}
