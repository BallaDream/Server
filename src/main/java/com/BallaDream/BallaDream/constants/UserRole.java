package com.BallaDream.BallaDream.constants;

import java.util.Arrays;

public enum UserRole {
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getUserRoleType() {
        return role;
    }

    public static UserRole valueOfRole(String label) {
        return Arrays.stream(values())
                .filter(value -> value.getUserRoleType().equals(label))
                .findAny()
                .orElse(null);
    }
}
