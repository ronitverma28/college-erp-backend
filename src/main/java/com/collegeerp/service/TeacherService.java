package com.collegeerp.service;

import com.collegeerp.dto.request.TeacherRequest;
import com.collegeerp.dto.response.TeacherResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TeacherService {
    TeacherResponse create(TeacherRequest request);

    TeacherResponse update(Long id, TeacherRequest request);

    TeacherResponse getById(Long id);

    Page<TeacherResponse> getAll(Pageable pageable);

    Page<TeacherResponse> getByBranch(Long branchId, Pageable pageable);

    void delete(Long id);
}
