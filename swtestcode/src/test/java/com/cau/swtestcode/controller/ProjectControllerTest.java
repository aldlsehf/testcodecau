package com.cau.swtestcode.controller;

import com.cau.swtestcode.domain.Member;
import com.cau.swtestcode.domain.Project;
import com.cau.swtestcode.domain.Users;
import com.cau.swtestcode.domain.enumClass.UserType;
import com.cau.swtestcode.dto.project.CreateProjectReq;
import com.cau.swtestcode.dto.project.ManageUserAccountReq;
import com.cau.swtestcode.repository.MemberRepository;
import com.cau.swtestcode.repository.ProjectRepository;
import com.cau.swtestcode.repository.UsersRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
    @DisplayName("프로젝트 생성 테스트 - 성공 케이스")
    void createProjectSuccessTest() throws Exception {
        // Given: 관리자 사용자 추가
        Users admin = new Users();
        admin.setUsername("admin");
        admin.setPassword("admin123");
        admin.setEmail("admin@example.com");
        admin.setAdmin(true);
        usersRepository.save(admin);

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
        testerReq.setProjectName("New Project");

        ManageUserAccountReq developerReq = new ManageUserAccountReq();
        developerReq.setUserEmail("developer@example.com");
        developerReq.setUserType(UserType.Developer);
        developerReq.setProjectName("New Project");


        ManageUserAccountReq projectLeaderReq = new ManageUserAccountReq();
        projectLeaderReq.setUserEmail("projectLeader@example.com");
        projectLeaderReq.setUserType(UserType.ProjectLeader);
        projectLeaderReq.setProjectName("New Project");


        CreateProjectReq createProjectReq = new CreateProjectReq();
        createProjectReq.setName("New Project");
        createProjectReq.setStartDate(new Date());
        createProjectReq.setEndDate(new Date());
        createProjectReq.setDescription("This is a new project.");
        createProjectReq.setManageUserAccounts(Arrays.asList(testerReq, developerReq, projectLeaderReq));

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(createProjectReq);

        mockMvc.perform(MockMvcRequestBuilders.post("/create/project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("프로젝트가 생성되었습니다."))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("프로젝트 리스트 조회 컨트롤러 테스트 - 성공 케이스")
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

        // When & Then: 프로젝트 리스트 조회
        mockMvc.perform(MockMvcRequestBuilders.get("/read/project-list/{userId}", user.getUserId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Project 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Project 2"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("프로젝트 리스트 조회 컨트롤러 테스트 - 실패 케이스 (존재하지 않는 사용자)")
    void readProjectListFailTest() throws Exception {
        // When & Then: 존재하지 않는 사용자의 프로젝트 리스트 조회 시 예외 발생
        mockMvc.perform(MockMvcRequestBuilders.get("/read/project-list/{userId}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

}