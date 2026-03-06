package com.collegeerp.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AttendancePercentageResponse {
    private Long studentId;
    private Long subjectId;
    private long totalClasses;
    private long presentCount;
    private double percentage;
}
