package com.cau.swtestcode.controller;

import com.cau.swtestcode.dto.project.CreateProjectReq;
import com.cau.swtestcode.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

}
