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

    //@Schema(description = "마일스톤 이름 ")
    private String milestoneName;
//    @Schema(description = "마일스톤 설명 ")
//    private String milestoneDes;


//    @Schema(description = "할당한 사람(Tester email)")
//    private String testerEmail;

    //@Schema(description = "우선 순위")
    private Priority priority;


    //@Schema(description = "컴넌트 타입")
    private Component component;

    //@Schema(description = "티켓 설명")
    private String ticketDescription;

    //@Schema(description = "티켓 제목")
    private String ticketTitle;
}

