package com.cau.swtestcode.dto.ticket.request;


import com.cau.swtestcode.domain.Milestone;
import com.cau.swtestcode.domain.Users;
import com.cau.swtestcode.domain.enumClass.Component;
import com.cau.swtestcode.domain.enumClass.Priority;
import com.cau.swtestcode.domain.enumClass.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTicketDto {

    //마일스톤
    private Milestone milestone;

    //할당한 사람
    private Users reporter;

    //할당된 사람
    private Users developer;

    //status
    private Status status;

    //우선순위
    private Priority priority;

    //티켓생성시간
    private LocalDateTime createdTime;

    //수정시간
    private LocalDateTime modifiedTime;

    //컴포넌트타입
    private Component component;

    //티켓설명
    private String description;

    //티켓제목
    private String title;
}
