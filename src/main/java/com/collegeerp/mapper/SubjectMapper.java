package com.collegeerp.mapper;

import com.collegeerp.dto.response.SubjectResponse;
import com.collegeerp.entity.Subject;

public final class SubjectMapper {

    private SubjectMapper() {
    }

    public static SubjectResponse toResponse(Subject subject) {
        return SubjectResponse.builder()
                .id(subject.getId())
                .name(subject.getName())
                .code(subject.getCode())
                .credits(subject.getCredits())
                .branchId(subject.getBranch().getId())
                .branchName(subject.getBranch().getName())
                .build();
    }
}
