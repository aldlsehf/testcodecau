package com.cau.swtestcode.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserReq {

    //사용자 이름
    private String username;
    //사용자 비밀번호
    private String password;
    //사용자 이메일
    private String email;
    //admin인지 아닌지
    private boolean admin;

}
