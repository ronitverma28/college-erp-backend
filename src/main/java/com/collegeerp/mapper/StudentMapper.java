package com.collegeerp.mapper;

import com.collegeerp.dto.response.StudentResponse;
import com.collegeerp.entity.Student;

public final class StudentMapper {

    private StudentMapper() {
    }

    public static StudentResponse toResponse(Student student) {
        return StudentResponse.builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .phone(student.getPhone())
                .enrollmentNumber(student.getEnrollmentNumber())
                .branchId(student.getBranch().getId())
                .branchName(student.getBranch().getName())
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())
                .build();
    }
}
