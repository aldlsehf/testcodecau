package com.cau.swtestcode.controller;

import com.cau.swtestcode.dto.ticket.request.CreateTicketDto;
import com.cau.swtestcode.dto.ticket.request.UpdateAssignReq;
import com.cau.swtestcode.dto.ticket.request.UpdateStatusReq;
import com.cau.swtestcode.dto.ticket.response.*;
import com.cau.swtestcode.service.TicketService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class TicketController {

    @Autowired
    private TicketService ticketService;
// ticket crud

    @PostMapping("/create/ticket/{projectId}/{userId}")
    /*@Operation(summary =  "티켓 생성 ", description = "1. projectId로 프로젝트 확인, 프로젝트 내 member list 가져오기, "
            +"2. 멤버인 애들중에 Develoer인 애들을 가져옴 : deveolpers"
            +"3. Develoer인 Member 티켓 리스트를 DevTicketList 라하자"
            +"DevTicketList 중에 dto의 Component 와 일치하는 티켓 리스트(티켓 수)를 HitCount"
            +"DevTicketList 중 assigned인 티켓 수를 Busycount"
            +"4. HitCount 가 제일 Dev가 한 명이면 그 Dev에게 assigned, "
            +"5. HitCount 가 여러 명이면  Busycount 가 제일 작은 Dev에게 assigned"
            +"6. 4,5 의 단계로도 구분 할 수 없다면 MemberId가 작은 Dev에게 assigned"
            ,

            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "생성됨 메세지"
                    )
            }
    )*/
    public void createTicket(@PathVariable Long projectId,@PathVariable Long userId , @RequestBody CreateTicketDto ticketDTO) {
        ticketService.createTicket(projectId,userId, ticketDTO);
    }




    @GetMapping("/read/assigned/ticket-list/{projectId}/{userId}")
    /*@Operation(summary = "프로젝트 내, 내 Assigned 티켓 리스트1 : Assigned 티켓 리스트 클릭 시 티켓들 떠야함", description = "projectId로 프로젝트 내 티켓 정보 반환",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "프로젝트 내 Assigned 티켓 리스트를 반환함."
                    )
            }
    )*/
    public List<AssignedTicketListRes> readAssignedTicketList(@PathVariable Long projectId, @PathVariable Long userId) {
        return ticketService.readAssignedTicketList(projectId, userId);
    }

    @GetMapping("/read/new/ticket-list/{projectId}/{userId}")
    /*@Operation(summary = "프로젝트 내, 내 new 티켓 리스트2 : NEW 티켓 리스트 클릭 시 티켓들 떠야함", description = "projectId로 프로젝트 내 티켓 정보 반환",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "프로젝트 내 티켓 리스트를 반환함."
                    )
            }
    )*/
    public List<NewTicketListRes> readNewTicketList(@PathVariable Long projectId, @PathVariable Long userId) {
        return ticketService.readNewTicketList(projectId, userId);
    }


    @GetMapping("/read/resolved/ticket-list/{projectId}/{userId}")
    /*@Operation(summary = "프로젝트 내, 내 resolved 티켓 리스트3", description = "projectId로 프로젝트 내 티켓 정보 반환",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "프로젝트 내 티켓 리스트를 반환함."
                    )
            }
    )*/
    public List<ResolvedTicketListRes> readResolvedTicketList(@PathVariable Long projectId, @PathVariable Long userId) {
        return ticketService.readResolvedTicketList(projectId, userId);
    }

    @GetMapping("/read/reopen/ticket-list/{projectId}/{userId}")
    /*@Operation(summary = "프로젝트 내, 내 reopen 티켓 리스트4", description = "projectId로 프로젝트 내 티켓 정보 반환",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "프로젝트 내 티켓 리스트를 반환함."
                    )
            }
    )*/
    public List<ReopenedTicketListRes> readReopenedTicketList(@PathVariable Long projectId, @PathVariable Long userId) {
        return ticketService.readReopenedTicketList(projectId, userId);
    }

    @GetMapping("/read/closed/ticket-list/{projectId}/{userId}")
    /*@Operation(summary = "프로젝트 내, 내 closed 티켓 리스트5", description = "projectId로 프로젝트 내 티켓 정보 반환",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "프로젝트 내 티켓 리스트를 반환함."
                    )
            }
    )*/
    public List<ClosedTicketListRes> readClosedTicketList(@PathVariable Long projectId, @PathVariable Long userId) {
        return ticketService.readClosedTicketList(projectId, userId);
    }




    @GetMapping("/read/all/ticket/{ticketId}")
    /*@Operation(summary =  "티켓 리스트 정보 ", description = "ticketId로  티켓 정보 반환",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "프로젝트 내 티켓 상세 정보: 댓글등을 반환함."
                    )
            }
    )*/
    public DetailTicketRes readTicket(@PathVariable Long ticketId) {
        return ticketService.readDetailTicket(ticketId);
    }


    @GetMapping("/read/ticket/statistics/{projectId}")
    /*@Operation(summary = "티켓 status 통계", description = "projectId로 프로젝트 내 티켓 정보 반환",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "이번 달 생성된 티켓의 Status (day/month)통계 정보를 반환함."
                    )
            }
    )*/
    public StaticsRes readTicketStatics(@PathVariable Long projectId) {
        return ticketService.readTicketStatics(projectId);
    }


    @PutMapping("/update/ticket/status/{projectId}/{ticketId}/{userId}")
    /*@Operation(summary = "U:티켓 업데이트 :status 변경", description = "ticketId로 티켓 status 변경",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "status 변경 성공"
                    )
            }
    )*/
    public String updateTicketStatus(@PathVariable Long projectId, @PathVariable Long ticketId, @PathVariable Long userId, @RequestBody UpdateStatusReq dto) {
        return ticketService.updateTicketStatus(projectId, ticketId, userId, dto);
    }

    @PutMapping("/update/ticket/assigned/{projectId}/{ticketId}/{userId}")
    /*@Operation(summary = "담당 개발자를 바꾸자", description = "만약 userId 가 PL 이면 assigned 된 Dev 가능: 지금 로그인된사람이 PL 이어야함",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "담당 개발자 변경 성공"
                    )
            }
    )*/
    public ResponseEntity<String> updateAssignedDeveloper(@PathVariable Long projectId, @PathVariable Long ticketId, @PathVariable Long userId, @RequestBody UpdateAssignReq dto) {
        try {
            String result = ticketService.updateAssignedDeveloper(projectId, ticketId, userId, dto);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 에러 발생");
        }
    }




    @DeleteMapping("/delete/{ticketId}")//
    public void deleteTicket(@PathVariable  Long ticketId){
        ticketService.deleteTicket( ticketId);

    }




    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }



}