package com.collegeerp.controller;

import com.collegeerp.dto.request.StudentRequest;
import com.collegeerp.dto.response.StudentResponse;
import com.collegeerp.entity.User;
import com.collegeerp.exception.ResourceNotFoundException;
import com.collegeerp.repository.UserRepository;
import com.collegeerp.service.StudentService;
import com.collegeerp.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final UserRepository userRepository;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<StudentResponse>> create(@Valid @RequestBody StudentRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Student created", studentService.create(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<StudentResponse>> update(@PathVariable Long id, @Valid @RequestBody StudentRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Student updated", studentService.update(id, request)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<ApiResponse<StudentResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Student fetched", studentService.getById(id)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<ApiResponse<Page<StudentResponse>>> getAll(
            @RequestParam(required = false) Long branchId,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<StudentResponse> page = branchId == null
                ? studentService.getAll(pageable)
                : studentService.getByBranch(branchId, pageable);
        return ResponseEntity.ok(ApiResponse.ok("Students fetched", page));
    }

    @GetMapping("/teacher/branch")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<Page<StudentResponse>>> getStudentsForTeacherBranch(
            Authentication authentication,
            @PageableDefault(size = 10) Pageable pageable) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (user.getLinkedTeacher() == null) {
            throw new ResourceNotFoundException("No teacher profile linked with user");
        }
        Page<StudentResponse> students = studentService.getByBranch(user.getLinkedTeacher().getBranch().getId(), pageable);
        return ResponseEntity.ok(ApiResponse.ok("Students fetched for teacher branch", students));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<StudentResponse>> getMyProfile(Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.ok("Student profile fetched", studentService.getMyProfile(authentication.getName())));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        studentService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Student deleted", null));
    }
}
