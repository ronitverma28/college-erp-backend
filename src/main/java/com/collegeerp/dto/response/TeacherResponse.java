package com.collegeerp.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Builder
public class TeacherResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Long branchId;
    private String branchName;
    private Set<Long> subjectIds;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
