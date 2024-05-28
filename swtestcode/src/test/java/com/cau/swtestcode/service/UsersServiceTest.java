package com.cau.swtestcode.service;

import com.cau.swtestcode.domain.Users;
import com.cau.swtestcode.dto.user.CreateUserReq;
import com.cau.swtestcode.dto.user.LoginRes;
import com.cau.swtestcode.repository.UsersRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class UsersServiceTest {

    @Autowired
    private UsersService usersService;

    @Autowired
    private UsersRepository usersRepository;

    @BeforeEach
    public void deleteAll() {
        usersRepository.deleteAll();
    }

    @Test
    @DisplayName("새로운 사용자 회원가입 서비스 테스트")
    void createNewUserTest() {
        // Given
        CreateUserReq createUserReq = new CreateUserReq();
        createUserReq.setUsername("seulhui");
        createUserReq.setPassword("1234");
        createUserReq.setEmail("seulhui@example.com");
        createUserReq.setAdmin(true);

        // When
        usersService.createUser(createUserReq);

        // Then
        Assertions.assertEquals(1L, usersRepository.count());
        Users user = usersRepository.findAll().get(0);
        Assertions.assertEquals("seulhui", user.getUsername());
        Assertions.assertEquals("1234", user.getPassword());
        Assertions.assertEquals("seulhui@example.com", user.getEmail());
        Assertions.assertTrue(user.isAdmin());
    }

    @Test
    @DisplayName("이미 존재하는 사용자 회원가입 서비스 테스트")
    void createExistingUserTest() {
        // Given: 이미 존재하는 사용자 추가
        Users existingUser = new Users();
        existingUser.setUsername("minseok1");
        existingUser.setPassword("1234");
        existingUser.setEmail("minseok@example.com");
        existingUser.setAdmin(false);
        usersRepository.save(existingUser);

        CreateUserReq createUserReq = new CreateUserReq();
        createUserReq.setUsername("minseok2");
        createUserReq.setPassword("1234");
        createUserReq.setEmail("minseok@example.com");//minseok1의 이메일(이미 가입되어있음. 중복 x)
        createUserReq.setAdmin(false);

        // When & Then
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            usersService.createUser(createUserReq);
        });

        Assertions.assertEquals("이미 존재하는 이메일입니다: minseok@example.com", exception.getMessage());
        Assertions.assertEquals(1L, usersRepository.count());//minseok2는 예외처리로 저장이 안되어야함. 따라서 count는 한명임
    }

    @Test
    @DisplayName("로그인 서비스 성공 테스트")
    void loginSuccessTest() {
        // Given
        Users user = new Users();
        user.setUsername("seulhui");
        user.setPassword("1234");
        user.setEmail("seulhui@example.com");
        user.setAdmin(false);
        usersRepository.save(user);

        // When
        LoginRes loginRes = usersService.login("seulhui@example.com", "1234");

        // Then
        Assertions.assertNotNull(loginRes);
        Assertions.assertEquals("seulhui", loginRes.getUsername());
        Assertions.assertEquals("seulhui@example.com", loginRes.getEmail());
        Assertions.assertFalse(loginRes.isAdmin());
    }

    @Test
    @DisplayName("로그인 서비스 실패 테스트 - 잘못된 비밀번호")
    void loginFailIncorrectPasswordTest() {
        // Given
        Users user = new Users();
        user.setUsername("minseok");
        user.setPassword("1234");
        user.setEmail("minseok@example.com");
        user.setAdmin(false);
        usersRepository.save(user);

        // When & Then
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            usersService.login("minseok@example.com", "FailPassword");
        });

        Assertions.assertEquals("비밀번호를 잘못 입력했습니다", exception.getMessage());
    }

    @Test
    @DisplayName("로그인 서비스 실패 테스트 - 존재하지 않는 이메일")
    void loginFailInvalidEmailTest() {
        // When & Then
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            usersService.login("nonexistent@example.com", "1234");
        });

        Assertions.assertEquals("아이디 또는 비밀번호를 잘못 입력했습니다", exception.getMessage());
    }
}