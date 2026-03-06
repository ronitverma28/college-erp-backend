package com.collegeerp.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubjectRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String code;
    @NotNull
    @Min(1)
    private Integer credits;
    @NotNull
    private Long branchId;
}
