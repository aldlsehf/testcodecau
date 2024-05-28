package com.cau.swtestcode.controller;


import com.cau.swtestcode.dto.user.CreateUserReq;
import com.cau.swtestcode.dto.user.LoginReq;
import com.cau.swtestcode.dto.user.LoginRes;
import com.cau.swtestcode.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UsersService userService;

    @PostMapping("/api/users/login")
    public ResponseEntity<?> login(@RequestBody LoginReq loginReq) {
        try {
            LoginRes loginRes = userService.login(loginReq.getId(), loginReq.getPassword());
            return ResponseEntity.ok(loginRes);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }


    @PostMapping("/api/users/signup")
    public ResponseEntity<String> signup(@RequestBody CreateUserReq createUserReq) {
        try {
            userService.createUser(createUserReq);
            return ResponseEntity.ok("회원가입 성공");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}