package com.cau.swtestcode.repository;

import com.cau.swtestcode.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Project findByProjectId(Long projectId); //Optional로 해야하는지 아닌지 결정해야함

    @Query("SELECT p FROM Project p JOIN p.members m WHERE m.user.userId = :userId")
    List<Project> findAllByUserId(@Param("userId") Long userId);// 5/29
}
