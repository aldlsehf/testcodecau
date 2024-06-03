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
public class ClosedTicketListRes {

    //@Schema(description = "티켓 ID")
    private Long ticketId;
    //@Schema(description = "티켓 이름")
    private String title;

    //@Schema(description = "티켓 우선 순위")
    private Priority priority;

    //@Schema(description = "티켓 생성 시간")
    private String createdTime;
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ClosedTicketList {
        private List<ClosedTicketListRes> closedTickets;
    }
}
