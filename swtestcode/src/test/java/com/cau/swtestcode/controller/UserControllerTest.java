package com.cau.swtestcode.controller;

import com.cau.swtestcode.dto.user.CreateUserReq;
import com.cau.swtestcode.dto.user.LoginReq;
import com.cau.swtestcode.repository.UsersRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
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

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsersRepository usersRepository;

    /*@BeforeEach
    public void deleteRepository() {
        usersRepository.deleteAll();
    }*/

    @Test
    @DisplayName("이미 존재하는 사용자 회원가입 테스트")
    void signupFailTest() throws Exception {
        CreateUserReq createUserReq = new CreateUserReq();
        createUserReq.setUsername("testuser");
        createUserReq.setPassword("password123");
        createUserReq.setEmail("testuser@example.com");
        createUserReq.setAdmin(false);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(createUserReq);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("이미 존재하는 이메일입니다: testuser@example.com"))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("새로운 사용자 회원가입 테스트")
    void signupSuccessTest() throws Exception {
        CreateUserReq createUserReq = new CreateUserReq();
        createUserReq.setUsername("minseok");
        createUserReq.setPassword("1234");
        createUserReq.setEmail("minseok@example.com");
        createUserReq.setAdmin(false);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(createUserReq);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("회원가입 성공"))
                .andDo(MockMvcResultHandlers.print());

        Assertions.assertEquals(usersRepository.count(), 2L); //미리 저장되어 있는 testuser와 방금 가입한 minseok 총 2명

    }

    @Test
    @DisplayName("로그인 테스트 성공")
    void loginSuccessTest() throws Exception {
        // Given
        LoginReq loginReq = new LoginReq();
        loginReq.setId("minseok@example.com");
        loginReq.setPassword("1234");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(loginReq);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("minseok"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("로그인 테스트 실패 - 비밀번호 잘못 입력")
    void loginFailTest() throws Exception {
        // Given
        LoginReq loginReq = new LoginReq();
        loginReq.setId("minseok@example.com");
        loginReq.setPassword("FailPassword");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(loginReq);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().string("비밀번호를 잘못 입력했습니다"))
                .andDo(MockMvcResultHandlers.print());
    }
}