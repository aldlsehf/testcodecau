package com.cau.swtestcode.controller;

import com.cau.swtestcode.dto.project.CreateProjectReq;
import com.cau.swtestcode.dto.project.ProjectListDto;
import com.cau.swtestcode.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class ProjectController {

    private ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/create/project")
    public ResponseEntity<String> createProject(@RequestBody CreateProjectReq createProjectReq) {
        try {
            projectService.createProject(createProjectReq);
            return ResponseEntity.ok("프로젝트가 생성되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/read/project-list/{userId}")
    public ResponseEntity<List<ProjectListDto>> readProjectList(@PathVariable Long userId) {
        try {
            List<ProjectListDto> projectList = projectService.readProjectList(userId);
            return ResponseEntity.ok(projectList);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
    }

}
