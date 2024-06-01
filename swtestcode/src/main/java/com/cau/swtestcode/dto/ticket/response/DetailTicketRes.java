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
public class DetailTicketRes {

    //"티켓 설명"
    private String description;

    //"티켓 상태"
    private String status;

    //"티켓 우선순위"
    private String priority;

    //"티켓 생성 시간"
    private String createdTime;

    //"티켓 수정 시간"
    private String modifiedTime;

    //"티켓 컴포넌트"
    private String component;

    //"티켓 담당자"
    private String developer;

    //"티켓 작성자"
    private String reporter;

    //"티켓 마일스톤"
    private String milestone;

    //"티켓 댓글 리스트"
    private List<CommentResponse> comments;

    @Getter
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CommentResponse {


        //"댓글 내용"
        private String content;

        //"댓글 작성 시간"
        private String timeStamp;
    }
}
