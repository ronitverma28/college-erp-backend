package com.collegeerp.service;

import com.collegeerp.dto.request.AttendanceRequest;
import com.collegeerp.dto.response.AttendancePercentageResponse;
import com.collegeerp.dto.response.AttendanceResponse;
import com.collegeerp.dto.response.BranchAttendanceReportResponse;

import java.util.List;

public interface AttendanceService {
    AttendanceResponse markAttendance(AttendanceRequest request, String username);

    List<AttendanceResponse> getByStudent(Long studentId, String username);

    List<AttendanceResponse> getBySubject(Long subjectId, String username);

    AttendancePercentageResponse getPercentage(Long studentId, Long subjectId, String username);

    BranchAttendanceReportResponse getBranchReport(Long branchId, String username);

    List<AttendanceResponse> getMyAttendance(String username);
}
