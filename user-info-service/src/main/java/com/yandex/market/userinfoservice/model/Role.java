package com.yandex.market.userinfoservice.model;

public enum Role {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String key;

    Role(String key) {
        this.key = key;
    }

    public static Role findRoleByKey(String key) {
        for (Role role : values()) {
            if (role.key.equals(key)) {
                return role;
            }
        }

        throw new IllegalArgumentException("There is no role for key: " + key);
    }
}
