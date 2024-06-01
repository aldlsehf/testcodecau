package com.cau.swtestcode.repository;

import com.cau.swtestcode.domain.Milestone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MilestoneRepository extends JpaRepository<Milestone, Long> {
}