package com.cau.swtestcode.repository;

import com.cau.swtestcode.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Project findByProjectId(Long projectId); //Optional로 해야하는지 아닌지 결정해야함
}
