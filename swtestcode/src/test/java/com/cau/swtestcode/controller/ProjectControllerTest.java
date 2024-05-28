package com.cau.swtestcode.controller;

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

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(createProjectReq);

        mockMvc.perform(MockMvcRequestBuilders.post("/create/project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("프로젝트가 생성되었습니다."))
                .andDo(MockMvcResultHandlers.print());

    }

}