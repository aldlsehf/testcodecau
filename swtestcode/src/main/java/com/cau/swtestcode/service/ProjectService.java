package com.cau.swtestcode.service;

import com.cau.swtestcode.domain.Member;
import com.cau.swtestcode.domain.Project;
import com.cau.swtestcode.domain.Users;
import com.cau.swtestcode.dto.project.CreateProjectReq;
import com.cau.swtestcode.dto.project.ManageUserAccountReq;
import com.cau.swtestcode.dto.project.ProjectListDto;
import com.cau.swtestcode.repository.MemberRepository;
import com.cau.swtestcode.repository.ProjectRepository;
import com.cau.swtestcode.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private UsersRepository usersRepository;


    @Transactional
    public void createProject(CreateProjectReq createProjectReq) throws Exception {
        Project project = new Project();
        project.setName(createProjectReq.getName());
        project.setStartDate(createProjectReq.getStartDate());
        project.setEndDate(createProjectReq.getEndDate());
        project.setDescription(createProjectReq.getDescription());

        projectRepository.save(project);

        for (ManageUserAccountReq manageUserAccountReq : createProjectReq.getManageUserAccounts()) {
            Users user = usersRepository.findByEmail(manageUserAccountReq.getUserEmail())
                    .orElseThrow(() -> new Exception("사용자를 찾을 수 없습니다."));

            Member member = new Member();
            member.setProject(project);
            member.setUser(user);
            member.setUserType(manageUserAccountReq.getUserType()); //추가
            memberRepository.save(member);
        }

    }

    @Transactional
    public List<ProjectListDto> readProjectList(Long userId) throws Exception {
        // 사용자가 존재하는지 확인
        if (!usersRepository.existsById(userId)) {
            throw new Exception("사용자를 찾을 수 없습니다.");
        }


        List<Project> projects = projectRepository.findAllByUserId(userId);
        return projects.stream()
                .map(project -> new ProjectListDto(project.getName(), project.getEndDate()))
                .collect(Collectors.toList());
    }


}
