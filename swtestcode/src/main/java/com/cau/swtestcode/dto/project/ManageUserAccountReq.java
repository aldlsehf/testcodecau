package com.cau.swtestcode.dto.project;

import com.cau.swtestcode.domain.enumClass.UserType;
import lombok.Data;
import lombok.Getter;

//admin이 project를 만들 때 추가할 계정
@Getter
@Data
public class ManageUserAccountReq {
    private String projectName;
    private String userEmail;
    private UserType userType; //수정했음
}
