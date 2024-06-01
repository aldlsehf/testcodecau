package com.cau.swtestcode.dto.ticket.response;


import com.cau.swtestcode.domain.enumClass.Priority;
import com.cau.swtestcode.domain.enumClass.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor

public class StaticsRes {

    //오늘 생성된 티켓의 상태별 개수
    private Map<Status, Long> todayStatusCount;

    //오늘 생성된 티켓의 우선순위별 개수
    private Map<Priority, Long> todayPriorityCount;

    //이번 달 생성된 티켓의 상태별 개수
    private Map<Status, Long> monthStatusCount;

    //이번 달 생성된 티켓의 우선순위별 개수
    private Map<Priority, Long> monthPriorityCount;
}
