package com.collegeerp.service;

import com.collegeerp.dto.request.BranchRequest;
import com.collegeerp.dto.response.BranchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BranchService {
    BranchResponse create(BranchRequest request);

    BranchResponse update(Long id, BranchRequest request);

    BranchResponse getById(Long id);

    Page<BranchResponse> getAll(Pageable pageable);

    void delete(Long id);
}
