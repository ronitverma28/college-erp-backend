package com.collegeerp.controller;

import com.collegeerp.dto.request.TeacherRequest;
import com.collegeerp.dto.response.TeacherResponse;
import com.collegeerp.service.TeacherService;
import com.collegeerp.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TeacherResponse>> create(@Valid @RequestBody TeacherRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Teacher created", teacherService.create(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TeacherResponse>> update(@PathVariable Long id, @Valid @RequestBody TeacherRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Teacher updated", teacherService.update(id, request)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<ApiResponse<TeacherResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Teacher fetched", teacherService.getById(id)));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<TeacherResponse>>> getAll(
            @RequestParam(required = false) Long branchId,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<TeacherResponse> page = branchId == null
                ? teacherService.getAll(pageable)
                : teacherService.getByBranch(branchId, pageable);
        return ResponseEntity.ok(ApiResponse.ok("Teachers fetched", page));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        teacherService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Teacher deleted", null));
    }
}
