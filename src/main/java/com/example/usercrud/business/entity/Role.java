package com.example.usercrud.business.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public enum Role {
    USER(Set.of(Permission.CREATE_USER)),
    ADMIN(Set.of(Permission.CREATE_USER, Permission.EDIT_USER, Permission.GET_USER, Permission.DELETE_USER));

    private final Set<Permission> permissions;

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermissions().stream()
            .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
            .collect(Collectors.toSet());
    }
}
