package com.dennis.ecommerce.auth.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreatedEvent {
    private String userId;
    private String email;
    private String role;
}
