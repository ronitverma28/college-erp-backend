package com.collegeerp.controller;

import com.collegeerp.dto.request.SubjectRequest;
import com.collegeerp.dto.response.SubjectResponse;
import com.collegeerp.service.SubjectService;
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

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SubjectResponse>> create(@Valid @RequestBody SubjectRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Subject created", subjectService.create(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SubjectResponse>> update(@PathVariable Long id, @Valid @RequestBody SubjectRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Subject updated", subjectService.update(id, request)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<ApiResponse<SubjectResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Subject fetched", subjectService.getById(id)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<ApiResponse<Page<SubjectResponse>>> getAll(
            @RequestParam(required = false) Long branchId,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<SubjectResponse> page = branchId == null
                ? subjectService.getAll(pageable)
                : subjectService.getByBranch(branchId, pageable);
        return ResponseEntity.ok(ApiResponse.ok("Subjects fetched", page));
    }

    @GetMapping("/teacher/me")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<List<SubjectResponse>>> getMySubjects(Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.ok("Teacher subjects fetched", subjectService.getByTeacher(authentication.getName())));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        subjectService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Subject deleted", null));
    }
}
