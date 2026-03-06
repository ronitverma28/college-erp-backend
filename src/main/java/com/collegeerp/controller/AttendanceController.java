package com.collegeerp.controller;

import com.collegeerp.dto.request.AttendanceRequest;
import com.collegeerp.dto.response.AttendancePercentageResponse;
import com.collegeerp.dto.response.AttendanceResponse;
import com.collegeerp.dto.response.BranchAttendanceReportResponse;
import com.collegeerp.service.AttendanceService;
import com.collegeerp.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<AttendanceResponse>> markAttendance(
            @Valid @RequestBody AttendanceRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.ok("Attendance marked",
                attendanceService.markAttendance(request, authentication.getName())));
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    public ResponseEntity<ApiResponse<List<AttendanceResponse>>> getByStudent(
            @PathVariable Long studentId,
            Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.ok("Attendance fetched",
                attendanceService.getByStudent(studentId, authentication.getName())));
    }

    @GetMapping("/subject/{subjectId}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<ApiResponse<List<AttendanceResponse>>> getBySubject(
            @PathVariable Long subjectId,
            Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.ok("Attendance fetched",
                attendanceService.getBySubject(subjectId, authentication.getName())));
    }

    @GetMapping("/percentage")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    public ResponseEntity<ApiResponse<AttendancePercentageResponse>> getPercentage(
            @RequestParam Long studentId,
            @RequestParam Long subjectId,
            Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.ok("Attendance percentage fetched",
                attendanceService.getPercentage(studentId, subjectId, authentication.getName())));
    }

    @GetMapping("/report/branch/{branchId}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<ApiResponse<BranchAttendanceReportResponse>> getBranchReport(
            @PathVariable Long branchId,
            Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.ok("Branch report fetched",
                attendanceService.getBranchReport(branchId, authentication.getName())));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<List<AttendanceResponse>>> getMyAttendance(Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.ok("My attendance fetched",
                attendanceService.getMyAttendance(authentication.getName())));
    }
}
