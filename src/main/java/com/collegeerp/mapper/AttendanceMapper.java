package com.collegeerp.mapper;

import com.collegeerp.dto.response.AttendanceResponse;
import com.collegeerp.entity.Attendance;

public final class AttendanceMapper {

    private AttendanceMapper() {
    }

    public static AttendanceResponse toResponse(Attendance attendance) {
        return AttendanceResponse.builder()
                .id(attendance.getId())
                .studentId(attendance.getStudent().getId())
                .studentName(attendance.getStudent().getFirstName() + " " + attendance.getStudent().getLastName())
                .subjectId(attendance.getSubject().getId())
                .subjectName(attendance.getSubject().getName())
                .teacherId(attendance.getTeacher().getId())
                .teacherName(attendance.getTeacher().getFirstName() + " " + attendance.getTeacher().getLastName())
                .date(attendance.getDate())
                .status(attendance.getStatus())
                .remarks(attendance.getRemarks())
                .build();
    }
}
