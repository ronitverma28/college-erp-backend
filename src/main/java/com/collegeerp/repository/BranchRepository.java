package com.collegeerp.repository;

import com.collegeerp.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BranchRepository extends JpaRepository<Branch, Long> {
    boolean existsByName(String name);

    boolean existsByCode(String code);

    Optional<Branch> findByCode(String code);
}
