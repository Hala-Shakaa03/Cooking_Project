package com.cooking.core;

public enum UserRole {
    CUSTOMER,
    CHEF,
    ADMIN,
    UNAUTHORIZED;
    
    public String toLower() {
        return this.name().toLowerCase();
    }
}
