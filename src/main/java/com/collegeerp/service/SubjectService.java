package com.collegeerp.service;

import com.collegeerp.dto.request.SubjectRequest;
import com.collegeerp.dto.response.SubjectResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SubjectService {
    SubjectResponse create(SubjectRequest request);

    SubjectResponse update(Long id, SubjectRequest request);

    SubjectResponse getById(Long id);

    Page<SubjectResponse> getAll(Pageable pageable);

    Page<SubjectResponse> getByBranch(Long branchId, Pageable pageable);

    List<SubjectResponse> getByTeacher(String username);

    void delete(Long id);
}
