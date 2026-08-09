package com.wordbridge.project.enums;

public enum UserRole {
    USER,
    COMPANY,
    ADMIN;

    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}
