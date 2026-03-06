package com.collegeerp.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BranchResponse {
    private Long id;
    private String name;
    private String code;
    private String description;
}
