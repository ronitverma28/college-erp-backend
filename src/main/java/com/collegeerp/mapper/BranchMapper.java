package com.collegeerp.mapper;

import com.collegeerp.dto.response.BranchResponse;
import com.collegeerp.entity.Branch;

public final class BranchMapper {

    private BranchMapper() {
    }

    public static BranchResponse toResponse(Branch branch) {
        return BranchResponse.builder()
                .id(branch.getId())
                .name(branch.getName())
                .code(branch.getCode())
                .description(branch.getDescription())
                .build();
    }
}
