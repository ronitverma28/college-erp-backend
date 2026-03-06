package com.collegeerp.dto.response;

import com.collegeerp.entity.enums.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private Role role;
    private Long linkedStudentId;
    private Long linkedTeacherId;
    private Boolean enabled;
}
