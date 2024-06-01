package com.cau.swtestcode.dto.ticket.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClosedTicketListRes {

    //"티켓 이름"
    private String title;

    //"티켓 상태"
    private String status;

    //"티켓 생성 시간"
    private String createdTime;
}
