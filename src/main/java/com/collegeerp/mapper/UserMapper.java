package com.collegeerp.mapper;

import com.collegeerp.dto.response.UserResponse;
import com.collegeerp.entity.User;

public final class UserMapper {

    private UserMapper() {
    }

    public static UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .enabled(user.getEnabled())
                .linkedStudentId(user.getLinkedStudent() != null ? user.getLinkedStudent().getId() : null)
                .linkedTeacherId(user.getLinkedTeacher() != null ? user.getLinkedTeacher().getId() : null)
                .build();
    }
}
