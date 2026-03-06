package com.collegeerp.service.impl;

import com.collegeerp.dto.request.StudentRequest;
import com.collegeerp.dto.response.StudentResponse;
import com.collegeerp.entity.Branch;
import com.collegeerp.entity.Student;
import com.collegeerp.entity.User;
import com.collegeerp.exception.DuplicateResourceException;
import com.collegeerp.exception.ResourceNotFoundException;
import com.collegeerp.mapper.StudentMapper;
import com.collegeerp.repository.BranchRepository;
import com.collegeerp.repository.StudentRepository;
import com.collegeerp.repository.UserRepository;
import com.collegeerp.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final BranchRepository branchRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public StudentResponse create(StudentRequest request) {
        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Student email already exists");
        }
        if (studentRepository.existsByEnrollmentNumber(request.getEnrollmentNumber())) {
            throw new DuplicateResourceException("Enrollment number already exists");
        }
        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + request.getBranchId()));
        Student student = Student.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .enrollmentNumber(request.getEnrollmentNumber())
                .branch(branch)
                .build();
        return StudentMapper.toResponse(studentRepository.save(student));
    }

    @Override
    @Transactional
    public StudentResponse update(Long id, StudentRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        if (!student.getEmail().equals(request.getEmail()) && studentRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Student email already exists");
        }
        if (!student.getEnrollmentNumber().equals(request.getEnrollmentNumber())
                && studentRepository.existsByEnrollmentNumber(request.getEnrollmentNumber())) {
            throw new DuplicateResourceException("Enrollment number already exists");
        }
        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + request.getBranchId()));

        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());
        student.setPhone(request.getPhone());
        student.setEnrollmentNumber(request.getEnrollmentNumber());
        student.setBranch(branch);
        return StudentMapper.toResponse(studentRepository.save(student));
    }

    @Override
    public StudentResponse getById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        return StudentMapper.toResponse(student);
    }

    @Override
    public Page<StudentResponse> getAll(Pageable pageable) {
        return studentRepository.findAll(pageable).map(StudentMapper::toResponse);
    }

    @Override
    public Page<StudentResponse> getByBranch(Long branchId, Pageable pageable) {
        return studentRepository.findByBranchId(branchId, pageable).map(StudentMapper::toResponse);
    }

    @Override
    public StudentResponse getMyProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (user.getLinkedStudent() == null) {
            throw new ResourceNotFoundException("No student profile linked with user");
        }
        return StudentMapper.toResponse(user.getLinkedStudent());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        studentRepository.delete(student);
    }
}
