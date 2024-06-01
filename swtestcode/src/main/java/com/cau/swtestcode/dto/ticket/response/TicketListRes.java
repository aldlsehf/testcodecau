package com.cau.swtestcode.dto.ticket.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketListRes {

    //"Assigned 티켓 리스트"
    private List<TicketInfo> assignedTickets;

    //"Closed 티켓 리스트"
    private List<TicketInfo> closedTickets;

    @Getter
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TicketInfo {
        //"티켓 이름"
        private String title;

        //"티켓 상태"
        private String status;

        //"티켓 생성 시간"
        private String createdTime;
    }
}
