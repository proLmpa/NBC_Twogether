package com.example.twogether.user.entity;

public enum UserRoleEnum {
    NOT_YET_VERIFIED(Authority.NOT_YET_VERIFIED),
    USER(Authority.USER),
    ADMIN(Authority.ADMIN);

    private final String authority;

    UserRoleEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    private static class Authority {
        public static final String NOT_YET_VERIFIED = "ROLE_NOT_YET_VERIFIED";
        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
    }
}