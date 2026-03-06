package com.collegeerp.service.impl;

import com.collegeerp.dto.request.AttendanceRequest;
import com.collegeerp.dto.response.AttendancePercentageResponse;
import com.collegeerp.dto.response.AttendanceResponse;
import com.collegeerp.dto.response.BranchAttendanceReportResponse;
import com.collegeerp.entity.Attendance;
import com.collegeerp.entity.Student;
import com.collegeerp.entity.Subject;
import com.collegeerp.entity.Teacher;
import com.collegeerp.entity.User;
import com.collegeerp.entity.enums.AttendanceStatus;
import com.collegeerp.entity.enums.Role;
import com.collegeerp.exception.BadRequestException;
import com.collegeerp.exception.DuplicateResourceException;
import com.collegeerp.exception.ResourceNotFoundException;
import com.collegeerp.mapper.AttendanceMapper;
import com.collegeerp.repository.AttendanceRepository;
import com.collegeerp.repository.BranchRepository;
import com.collegeerp.repository.StudentRepository;
import com.collegeerp.repository.SubjectRepository;
import com.collegeerp.repository.UserRepository;
import com.collegeerp.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;
    private final BranchRepository branchRepository;

    @Override
    @Transactional
    public AttendanceResponse markAttendance(AttendanceRequest request, String username) {
        User user = getUser(username);
        if (user.getRole() != Role.ADMIN && user.getRole() != Role.TEACHER) {
            throw new BadRequestException("Only teacher or admin can mark attendance");
        }
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + request.getStudentId()));
        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + request.getSubjectId()));
        Teacher teacher = resolveTeacherForAttendance(user);

        if (!student.getBranch().getId().equals(subject.getBranch().getId())) {
            throw new BadRequestException("Student and subject must belong to same branch");
        }
        if (user.getRole() == Role.TEACHER) {
            if (!teacher.getBranch().getId().equals(student.getBranch().getId())) {
                throw new BadRequestException("Teacher can only mark attendance for own branch");
            }
            boolean assigned = teacher.getSubjects().stream().anyMatch(s -> s.getId().equals(subject.getId()));
            if (!assigned) {
                throw new BadRequestException("Teacher is not assigned to this subject");
            }
        }
        if (attendanceRepository.existsByStudentIdAndSubjectIdAndDate(
                request.getStudentId(), request.getSubjectId(), request.getDate())) {
            throw new DuplicateResourceException("Attendance already marked for this student/subject/date");
        }
        Attendance attendance = Attendance.builder()
                .student(student)
                .subject(subject)
                .teacher(teacher)
                .date(request.getDate())
                .status(request.getStatus())
                .remarks(request.getRemarks())
                .build();
        return AttendanceMapper.toResponse(attendanceRepository.save(attendance));
    }

    @Override
    public List<AttendanceResponse> getByStudent(Long studentId, String username) {
        User user = getUser(username);
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));
        if (user.getRole() == Role.STUDENT) {
            if (user.getLinkedStudent() == null || !user.getLinkedStudent().getId().equals(studentId)) {
                throw new BadRequestException("Student can only view own attendance");
            }
        }
        if (user.getRole() == Role.TEACHER) {
            if (user.getLinkedTeacher() == null ||
                    !user.getLinkedTeacher().getBranch().getId().equals(student.getBranch().getId())) {
                throw new BadRequestException("Teacher can only view students in own branch");
            }
        }
        return attendanceRepository.findByStudentId(studentId).stream().map(AttendanceMapper::toResponse).toList();
    }

    @Override
    public List<AttendanceResponse> getBySubject(Long subjectId, String username) {
        User user = getUser(username);
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + subjectId));
        if (user.getRole() == Role.TEACHER) {
            if (user.getLinkedTeacher() == null ||
                    !user.getLinkedTeacher().getBranch().getId().equals(subject.getBranch().getId())) {
                throw new BadRequestException("Teacher can only view subjects in own branch");
            }
        } else if (user.getRole() == Role.STUDENT) {
            throw new BadRequestException("Student is not allowed to query full subject attendance");
        }
        return attendanceRepository.findBySubjectId(subjectId).stream().map(AttendanceMapper::toResponse).toList();
    }

    @Override
    public AttendancePercentageResponse getPercentage(Long studentId, Long subjectId, String username) {
        List<AttendanceResponse> records = getByStudent(studentId, username)
                .stream()
                .filter(a -> a.getSubjectId().equals(subjectId))
                .toList();
        long total = records.size();
        long present = records.stream().filter(a -> a.getStatus() == AttendanceStatus.PRESENT).count();
        double percentage = total == 0 ? 0.0 : (present * 100.0) / total;
        return AttendancePercentageResponse.builder()
                .studentId(studentId)
                .subjectId(subjectId)
                .totalClasses(total)
                .presentCount(present)
                .percentage(percentage)
                .build();
    }

    @Override
    public BranchAttendanceReportResponse getBranchReport(Long branchId, String username) {
        User user = getUser(username);
        if (user.getRole() == Role.TEACHER) {
            if (user.getLinkedTeacher() == null || !user.getLinkedTeacher().getBranch().getId().equals(branchId)) {
                throw new BadRequestException("Teacher can only view own branch report");
            }
        } else if (user.getRole() == Role.STUDENT) {
            throw new BadRequestException("Student is not allowed to view branch report");
        }
        String branchName = branchRepository.findById(branchId)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + branchId))
                .getName();
        List<Attendance> records = attendanceRepository.findByStudentBranchId(branchId);
        long total = records.size();
        long present = records.stream().filter(a -> a.getStatus() == AttendanceStatus.PRESENT).count();
        long absent = total - present;
        double percentage = total == 0 ? 0.0 : (present * 100.0) / total;

        return BranchAttendanceReportResponse.builder()
                .branchId(branchId)
                .branchName(branchName)
                .totalRecords(total)
                .presentCount(present)
                .absentCount(absent)
                .presentPercentage(percentage)
                .build();
    }

    @Override
    public List<AttendanceResponse> getMyAttendance(String username) {
        User user = getUser(username);
        if (user.getLinkedStudent() == null) {
            throw new ResourceNotFoundException("No student profile linked with user");
        }
        return attendanceRepository.findByStudentId(user.getLinkedStudent().getId())
                .stream()
                .map(AttendanceMapper::toResponse)
                .toList();
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private Teacher resolveTeacherForAttendance(User user) {
        if (user.getRole() == Role.TEACHER) {
            if (user.getLinkedTeacher() == null) {
                throw new ResourceNotFoundException("No teacher profile linked with user");
            }
            return user.getLinkedTeacher();
        }
        if (user.getRole() == Role.ADMIN) {
            if (user.getLinkedTeacher() == null) {
                throw new BadRequestException("Admin marking attendance must be linked with teacher profile");
            }
            return user.getLinkedTeacher();
        }
        throw new BadRequestException("Invalid role for marking attendance");
    }
}
