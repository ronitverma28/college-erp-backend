package com.collegeerp.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class StudentResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String enrollmentNumber;
    private Long branchId;
    private String branchName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
