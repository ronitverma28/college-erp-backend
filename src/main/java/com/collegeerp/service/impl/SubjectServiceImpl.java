package com.collegeerp.service.impl;

import com.collegeerp.dto.request.SubjectRequest;
import com.collegeerp.dto.response.SubjectResponse;
import com.collegeerp.entity.Branch;
import com.collegeerp.entity.Subject;
import com.collegeerp.entity.User;
import com.collegeerp.exception.DuplicateResourceException;
import com.collegeerp.exception.ResourceNotFoundException;
import com.collegeerp.mapper.SubjectMapper;
import com.collegeerp.repository.BranchRepository;
import com.collegeerp.repository.SubjectRepository;
import com.collegeerp.repository.UserRepository;
import com.collegeerp.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final BranchRepository branchRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public SubjectResponse create(SubjectRequest request) {
        if (subjectRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Subject code already exists");
        }
        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + request.getBranchId()));
        Subject subject = Subject.builder()
                .name(request.getName())
                .code(request.getCode())
                .credits(request.getCredits())
                .branch(branch)
                .build();
        return SubjectMapper.toResponse(subjectRepository.save(subject));
    }

    @Override
    @Transactional
    public SubjectResponse update(Long id, SubjectRequest request) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + id));
        if (!subject.getCode().equals(request.getCode()) && subjectRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Subject code already exists");
        }
        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + request.getBranchId()));
        subject.setName(request.getName());
        subject.setCode(request.getCode());
        subject.setCredits(request.getCredits());
        subject.setBranch(branch);
        return SubjectMapper.toResponse(subjectRepository.save(subject));
    }

    @Override
    public SubjectResponse getById(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + id));
        return SubjectMapper.toResponse(subject);
    }

    @Override
    public Page<SubjectResponse> getAll(Pageable pageable) {
        return subjectRepository.findAll(pageable).map(SubjectMapper::toResponse);
    }

    @Override
    public Page<SubjectResponse> getByBranch(Long branchId, Pageable pageable) {
        return subjectRepository.findByBranchId(branchId, pageable).map(SubjectMapper::toResponse);
    }

    @Override
    public List<SubjectResponse> getByTeacher(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (user.getLinkedTeacher() == null) {
            throw new ResourceNotFoundException("No teacher profile linked with user");
        }
        return subjectRepository.findByTeachersId(user.getLinkedTeacher().getId())
                .stream()
                .map(SubjectMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + id));
        subjectRepository.delete(subject);
    }
}
