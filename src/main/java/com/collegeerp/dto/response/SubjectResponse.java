package com.collegeerp.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubjectResponse {
    private Long id;
    private String name;
    private String code;
    private Integer credits;
    private Long branchId;
    private String branchName;
}
