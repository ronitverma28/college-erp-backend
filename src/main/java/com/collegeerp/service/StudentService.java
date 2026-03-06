package com.collegeerp.service;

import com.collegeerp.dto.request.StudentRequest;
import com.collegeerp.dto.response.StudentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentService {
    StudentResponse create(StudentRequest request);

    StudentResponse update(Long id, StudentRequest request);

    StudentResponse getById(Long id);

    Page<StudentResponse> getAll(Pageable pageable);

    Page<StudentResponse> getByBranch(Long branchId, Pageable pageable);

    StudentResponse getMyProfile(String username);

    void delete(Long id);
}
