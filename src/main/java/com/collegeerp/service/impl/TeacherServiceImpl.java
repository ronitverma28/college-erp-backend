package com.collegeerp.service.impl;

import com.collegeerp.dto.request.TeacherRequest;
import com.collegeerp.dto.response.TeacherResponse;
import com.collegeerp.entity.Branch;
import com.collegeerp.entity.Subject;
import com.collegeerp.entity.Teacher;
import com.collegeerp.exception.DuplicateResourceException;
import com.collegeerp.exception.ResourceNotFoundException;
import com.collegeerp.mapper.TeacherMapper;
import com.collegeerp.repository.BranchRepository;
import com.collegeerp.repository.SubjectRepository;
import com.collegeerp.repository.TeacherRepository;
import com.collegeerp.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final BranchRepository branchRepository;
    private final SubjectRepository subjectRepository;

    @Override
    @Transactional
    public TeacherResponse create(TeacherRequest request) {
        if (teacherRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Teacher email already exists");
        }
        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + request.getBranchId()));
        Set<Subject> subjects = resolveSubjects(request);
        Teacher teacher = Teacher.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .branch(branch)
                .subjects(subjects)
                .build();
        return TeacherMapper.toResponse(teacherRepository.save(teacher));
    }

    @Override
    @Transactional
    public TeacherResponse update(Long id, TeacherRequest request) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + id));
        if (!teacher.getEmail().equals(request.getEmail()) && teacherRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Teacher email already exists");
        }
        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + request.getBranchId()));
        Set<Subject> subjects = resolveSubjects(request);

        teacher.setFirstName(request.getFirstName());
        teacher.setLastName(request.getLastName());
        teacher.setEmail(request.getEmail());
        teacher.setPhone(request.getPhone());
        teacher.setBranch(branch);
        teacher.setSubjects(subjects);
        return TeacherMapper.toResponse(teacherRepository.save(teacher));
    }

    @Override
    public TeacherResponse getById(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + id));
        return TeacherMapper.toResponse(teacher);
    }

    @Override
    public Page<TeacherResponse> getAll(Pageable pageable) {
        return teacherRepository.findAll(pageable).map(TeacherMapper::toResponse);
    }

    @Override
    public Page<TeacherResponse> getByBranch(Long branchId, Pageable pageable) {
        return teacherRepository.findByBranchId(branchId, pageable).map(TeacherMapper::toResponse);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + id));
        teacherRepository.delete(teacher);
    }

    private Set<Subject> resolveSubjects(TeacherRequest request) {
        Set<Subject> subjects = new HashSet<>();
        if (request.getSubjectIds() == null || request.getSubjectIds().isEmpty()) {
            return subjects;
        }
        for (Long subjectId : request.getSubjectIds()) {
            Subject subject = subjectRepository.findById(subjectId)
                    .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + subjectId));
            subjects.add(subject);
        }
        return subjects;
    }
}
