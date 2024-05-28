package com.cau.swtestcode.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginReq {

    //로그인 아이디(이메일)
    private String id;
    //로그인 비밀번호
    private String password;
}
