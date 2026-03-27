package com.dennis.ecommerce.auth.dto;

import com.dennis.ecommerce.auth.domain.entity.User;
import lombok.Getter;

@Getter
public class UserResponse {
    private final String id;
    private final String email;
    private final String role;
    private final boolean active;

    public UserResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.role = user.getRole().name();
        this.active = user.isActive();
    }
}