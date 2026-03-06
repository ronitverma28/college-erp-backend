package com.collegeerp.mapper;

import com.collegeerp.dto.response.TeacherResponse;
import com.collegeerp.entity.Teacher;

import java.util.stream.Collectors;

public final class TeacherMapper {

    private TeacherMapper() {
    }

    public static TeacherResponse toResponse(Teacher teacher) {
        return TeacherResponse.builder()
                .id(teacher.getId())
                .firstName(teacher.getFirstName())
                .lastName(teacher.getLastName())
                .email(teacher.getEmail())
                .phone(teacher.getPhone())
                .branchId(teacher.getBranch().getId())
                .branchName(teacher.getBranch().getName())
                .subjectIds(teacher.getSubjects()
                        .stream()
                        .map(subject -> subject.getId())
                        .collect(Collectors.toSet()))
                .createdAt(teacher.getCreatedAt())
                .updatedAt(teacher.getUpdatedAt())
                .build();
    }
}
