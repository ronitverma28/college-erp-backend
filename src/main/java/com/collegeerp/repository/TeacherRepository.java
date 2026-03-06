package com.collegeerp.repository;

import com.collegeerp.entity.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    boolean existsByEmail(String email);

    Optional<Teacher> findByEmail(String email);

    Page<Teacher> findByBranchId(Long branchId, Pageable pageable);
}
