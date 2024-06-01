package com.cau.swtestcode.controller;

import com.cau.swtestcode.dto.ticket.request.CreateTicketDto;
import com.cau.swtestcode.dto.ticket.request.UpdateStatusReq;
import com.cau.swtestcode.dto.ticket.response.AssignedTicketListRes;
import com.cau.swtestcode.dto.ticket.response.DetailTicketRes;
import com.cau.swtestcode.dto.ticket.response.StaticsRes;
import com.cau.swtestcode.dto.ticket.response.TicketListRes;
import com.cau.swtestcode.service.TicketService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class TicketController {

    @Autowired
    private TicketService ticketService;
// ticket crud

    //summary =  "티켓 생성 ", description = "projectId로 프로젝트 내 티켓 정보 반환"
    @PostMapping("/create/ticket/{projectId}")
    public void createTicket(@PathVariable Long projectId, @RequestBody CreateTicketDto ticketDTO) {
         ticketService.createTicket(projectId, ticketDTO);
    }

    //summary = "프로젝트 내, 내 티켓 리스트", description = "projectId로 프로젝트 내 티켓 정보 반환"
    @GetMapping("/read/ticket-list/{projectId}/{userId}")
    public TicketListRes readTicketList(@PathVariable Long projectId, @PathVariable Long userId) {
        return ticketService.readTicketList(projectId, userId);
    }


    //"프로젝트 내, 내 Assigned티켓 리스트", description = "projectId로 프로젝트 내 티켓 정보 반환"
    @GetMapping("/read/assigned/ticket-list/{projectId}/{userId}")
    public List<AssignedTicketListRes> readAssignedTicketList(@PathVariable Long projectId, @PathVariable Long userId) {
        return ticketService.readAssignedTicketList(projectId, userId);
    }


    //summary =  "티켓 리스트 정보 ", description = "ticketId로  티켓 정보 반환",
    @GetMapping("/read/ticket/{ticketId}")
    public DetailTicketRes readTicket(@PathVariable Long ticketId) {
         return ticketService.readDetailTicket(ticketId);
    }



    //"티켓 status 통계", description = "projectId로 프로젝트 내 티켓 정보 반환",
    @GetMapping("/read/ticket/statistics/{projectId}")
    public StaticsRes readTicketStatics(@PathVariable Long projectId) {
        return ticketService.readTicketStatics(projectId);
    }


    //summary = "U:티켓 업데이트 :status 변경", description = "ticketId로 티켓 status 변경",
    @PutMapping("/update/ticket/status/{projectId}/{ticketId}/{userId}")
    public String updateTicketStatus(@PathVariable Long projectId, @PathVariable Long ticketId, @PathVariable Long userId, @RequestBody UpdateStatusReq dto) {
        return ticketService.updateTicketStatus(projectId, ticketId, userId, dto);
    }




    @DeleteMapping("/delete/{ticketId}")
    public void deleteTicket(@PathVariable  Long ticketId){
        ticketService.deleteTicket( ticketId);

    }





    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }



}
