package com.cau.swtestcode.dto.ticket.request;

import com.cau.swtestcode.domain.enumClass.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStatusReq {

    //새로운 티켓 상태
    private Status status;
}
