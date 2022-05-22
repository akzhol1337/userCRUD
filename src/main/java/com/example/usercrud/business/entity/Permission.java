package com.example.usercrud.business.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Permission {
    CREATE_USER("create"),
    EDIT_USER("edit"),
    DELETE_USER("delete"),
    GET_USER("get_user");

    private final String permission;
}
