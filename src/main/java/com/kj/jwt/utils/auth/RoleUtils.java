package com.kj.jwt.utils.auth;

import com.kj.jwt.models.Role;
import com.kj.jwt.repositories.RoleRepository;
import com.kj.jwt.utils.enums.RoleEnum;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RoleUtils {
    public static Set<Role> rolesFromStrings(RoleRepository repository, Set<String> roleNames) {
        Set<Role> roles = new HashSet<>();

        if(roleNames == null) {
            roles.add(findRoleByEnum(repository, RoleEnum.ROLE_USER));
        }
        else {
            roleNames.forEach(roleName -> {
                switch (roleName) {
                    case "ADMIN" -> roles.add(findRoleByEnum(repository, RoleEnum.ROLE_ADMIN));
                    case "MOD" -> roles.add(findRoleByEnum(repository, RoleEnum.ROLE_MOD));
                    default -> roles.add(findRoleByEnum(repository, RoleEnum.ROLE_USER));
                }
            });
        }

        return roles;
    }

    private static Role findRoleByEnum(RoleRepository repository, RoleEnum roleEnum) {
        return repository.findByName(roleEnum).orElseThrow(() -> new RuntimeException("Error: Role not found!"));
    }

    public static List<String> roleNamesFromUserDetails(UserDetailsImpl userDetails) {
        return userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }
}
