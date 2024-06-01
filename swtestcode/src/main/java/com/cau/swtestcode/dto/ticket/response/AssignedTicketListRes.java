package com.cau.swtestcode.dto.ticket.response;


import com.cau.swtestcode.domain.enumClass.Priority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignedTicketListRes {

    //티켓 이름
    private String title;

    //티켓 우선 순위
    private Priority priority;

    //티켓 생성 시간
    private String createdTime;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AssignedTicketList {
        private List<AssignedTicketListRes> assignedTickets;
    }
}
