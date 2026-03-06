package com.collegeerp.repository;

import com.collegeerp.entity.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    boolean existsByCode(String code);

    Optional<Subject> findByCode(String code);

    Page<Subject> findByBranchId(Long branchId, Pageable pageable);

    List<Subject> findByTeachersId(Long teacherId);
}
