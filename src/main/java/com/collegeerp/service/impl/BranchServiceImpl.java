package com.collegeerp.service.impl;

import com.collegeerp.dto.request.BranchRequest;
import com.collegeerp.dto.response.BranchResponse;
import com.collegeerp.entity.Branch;
import com.collegeerp.exception.DuplicateResourceException;
import com.collegeerp.exception.ResourceNotFoundException;
import com.collegeerp.mapper.BranchMapper;
import com.collegeerp.repository.BranchRepository;
import com.collegeerp.service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;

    @Override
    @Transactional
    public BranchResponse create(BranchRequest request) {
        if (branchRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Branch name already exists");
        }
        if (branchRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Branch code already exists");
        }
        Branch branch = Branch.builder()
                .name(request.getName())
                .code(request.getCode())
                .description(request.getDescription())
                .build();
        return BranchMapper.toResponse(branchRepository.save(branch));
    }

    @Override
    @Transactional
    public BranchResponse update(Long id, BranchRequest request) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + id));
        if (!branch.getName().equals(request.getName()) && branchRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Branch name already exists");
        }
        if (!branch.getCode().equals(request.getCode()) && branchRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Branch code already exists");
        }
        branch.setName(request.getName());
        branch.setCode(request.getCode());
        branch.setDescription(request.getDescription());
        return BranchMapper.toResponse(branchRepository.save(branch));
    }

    @Override
    public BranchResponse getById(Long id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + id));
        return BranchMapper.toResponse(branch);
    }

    @Override
    public Page<BranchResponse> getAll(Pageable pageable) {
        return branchRepository.findAll(pageable).map(BranchMapper::toResponse);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + id));
        branchRepository.delete(branch);
    }
}
