package com.collegeerp.dto.response;

import com.collegeerp.entity.enums.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponse {
    private String token;
    private String username;
    private Role role;
}
