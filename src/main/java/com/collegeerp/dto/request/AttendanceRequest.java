package com.collegeerp.dto.request;

import com.collegeerp.entity.enums.AttendanceStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AttendanceRequest {
    @NotNull
    private Long studentId;
    @NotNull
    private Long subjectId;
    @NotNull
    private LocalDate date;
    @NotNull
    private AttendanceStatus status;
    private String remarks;
}
