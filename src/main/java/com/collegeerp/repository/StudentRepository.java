package com.collegeerp.repository;

import com.collegeerp.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByEmail(String email);

    boolean existsByEnrollmentNumber(String enrollmentNumber);

    Optional<Student> findByEmail(String email);

    Page<Student> findByBranchId(Long branchId, Pageable pageable);
}
