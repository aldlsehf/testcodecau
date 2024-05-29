package com.cau.swtestcode.service;

import com.cau.swtestcode.domain.Member;
import com.cau.swtestcode.domain.Project;
import com.cau.swtestcode.domain.Users;
import com.cau.swtestcode.domain.enumClass.UserType;
import com.cau.swtestcode.dto.project.CreateProjectReq;
import com.cau.swtestcode.dto.project.ManageUserAccountReq;
import com.cau.swtestcode.dto.project.ProjectListDto;
import com.cau.swtestcode.repository.MemberRepository;
import com.cau.swtestcode.repository.ProjectRepository;
import com.cau.swtestcode.repository.UsersRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Transactional
@SpringBootTest
class ProjectServiceTest {
    @Autowired
    private ProjectService projectService;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void deleteAll() {
        memberRepository.deleteAll();
        projectRepository.deleteAll();
        usersRepository.deleteAll();
    }

    @Test
    @DisplayName("프로젝트 생성 서비스 테스트")
    void createProjectSuccessTest() throws Exception {

        // Given: 일반 사용자 추가 (테스터, 개발자, 프로젝트 리더)
        Users tester = new Users();
        tester.setUsername("tester");
        tester.setPassword("password123");
        tester.setEmail("tester@example.com");
        tester.setAdmin(false);
        usersRepository.save(tester);

        Users developer = new Users();
        developer.setUsername("developer");
        developer.setPassword("password123");
        developer.setEmail("developer@example.com");
        developer.setAdmin(false);
        usersRepository.save(developer);

        Users projectLeader = new Users();
        projectLeader.setUsername("projectLeader");
        projectLeader.setPassword("password123");
        projectLeader.setEmail("projectLeader@example.com");
        projectLeader.setAdmin(false);
        usersRepository.save(projectLeader);

        // Given: 프로젝트 생성 요청
        ManageUserAccountReq testerReq = new ManageUserAccountReq();
        testerReq.setUserEmail("tester@example.com");
        testerReq.setUserType(UserType.Tester);

        ManageUserAccountReq developerReq = new ManageUserAccountReq();
        developerReq.setUserEmail("developer@example.com");
        developerReq.setUserType(UserType.Developer);

        ManageUserAccountReq projectLeaderReq = new ManageUserAccountReq();
        projectLeaderReq.setUserEmail("projectLeader@example.com");
        projectLeaderReq.setUserType(UserType.ProjectLeader);

        CreateProjectReq createProjectReq = new CreateProjectReq();
        createProjectReq.setName("New Project");
        createProjectReq.setStartDate(new Date());
        createProjectReq.setEndDate(new Date());
        createProjectReq.setDescription("This is a new project.");
        createProjectReq.setManageUserAccounts(Arrays.asList(testerReq, developerReq, projectLeaderReq));

        // When
        projectService.createProject(createProjectReq);

        // Then
        Assertions.assertEquals(projectRepository.count(), 1L);
        Assertions.assertEquals(memberRepository.count(), 3L);

        Project createdProject = projectRepository.findAll().get(0);
        Assertions.assertEquals(createdProject.getName(), "New Project");

        List<Member> testerMembers = memberRepository.findByUserEmail("tester@example.com");
        List<Member> developerMembers = memberRepository.findByUserEmail("developer@example.com");
        List<Member> projectLeaderMembers = memberRepository.findByUserEmail("projectLeader@example.com");

        Assertions.assertFalse(testerMembers.isEmpty());
        Assertions.assertFalse(developerMembers.isEmpty());
        Assertions.assertFalse(projectLeaderMembers.isEmpty());

        Assertions.assertEquals(testerMembers.get(0).getProject().getProjectId(), createdProject.getProjectId());
        Assertions.assertEquals(developerMembers.get(0).getProject().getProjectId(), createdProject.getProjectId());
        Assertions.assertEquals(projectLeaderMembers.get(0).getProject().getProjectId(), createdProject.getProjectId());
    }

    @Test
    @DisplayName("프로젝트 리스트 조회 서비스 테스트 - 성공 케이스")
    void readProjectListSuccessTest() throws Exception {
        // Given: 사용자 추가
        Users user = new Users();
        user.setUsername("user");
        user.setPassword("password123");
        user.setEmail("user@example.com");
        user.setAdmin(false);
        user = usersRepository.save(user); // 저장 후 반환된 객체 사용

        // Given: 프로젝트 추가
        Project project1 = new Project();
        project1.setName("Project 1");
        project1.setStartDate(new Date());
        project1.setEndDate(new Date());
        project1.setDescription("Description 1");
        projectRepository.save(project1);
        Member member1 = new Member(user, project1, UserType.Developer);
        memberRepository.save(member1);

        Project project2 = new Project();
        project2.setName("Project 2");
        project2.setStartDate(new Date());
        project2.setEndDate(new Date());
        project2.setDescription("Description 2");
        projectRepository.save(project2);
        Member member2 = new Member(user, project2, UserType.Tester);
        memberRepository.save(member2);

        // When: 프로젝트 리스트 조회
        List<ProjectListDto> projectList = projectService.readProjectList(user.getUserId());

        // Then: 검증
        Assertions.assertEquals(2, projectList.size());
        Assertions.assertTrue(projectList.stream().anyMatch(p -> p.getName().equals("Project 1")));
        Assertions.assertTrue(projectList.stream().anyMatch(p -> p.getName().equals("Project 2")));
    }

    @Test
    @DisplayName("프로젝트 리스트 조회 서비스 테스트 - 실패 케이스 (존재하지 않는 사용자)")
    void readProjectListFailTest() {
        // When & Then: 존재하지 않는 사용자의 프로젝트 리스트 조회 시 예외 발생
        Exception exception = Assertions.assertThrows(Exception.class, () -> {
            projectService.readProjectList(999L);//db에 없는 사용자 id
        });

        Assertions.assertEquals("사용자를 찾을 수 없습니다.", exception.getMessage());
    }
}