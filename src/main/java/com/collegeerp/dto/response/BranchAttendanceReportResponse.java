package com.collegeerp.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BranchAttendanceReportResponse {
    private Long branchId;
    private String branchName;
    private long totalRecords;
    private long presentCount;
    private long absentCount;
    private double presentPercentage;
}
