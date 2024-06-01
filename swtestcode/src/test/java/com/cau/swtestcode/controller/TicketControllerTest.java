package com.cau.swtestcode.controller;

import com.cau.swtestcode.domain.*;
import com.cau.swtestcode.domain.enumClass.Component;
import com.cau.swtestcode.domain.enumClass.Priority;
import com.cau.swtestcode.domain.enumClass.Status;
import com.cau.swtestcode.domain.enumClass.UserType;
import com.cau.swtestcode.dto.ticket.request.CreateTicketDto;
import com.cau.swtestcode.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class TicketControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MilestoneRepository milestoneRepository;

    @Autowired
    private TicketRepository ticketRepository;


    @BeforeEach
    public void deleteAll() {
        ticketRepository.deleteAll();
        memberRepository.deleteAll();
        milestoneRepository.deleteAll();
        projectRepository.deleteAll();
        usersRepository.deleteAll();
    }


    @Test
    @DisplayName("티켓 생성 컨트롤러 테스트 - 성공 케이스")
    void createTicketSuccessTest() throws Exception {
        // Given: 프로젝트와 사용자 추가
        Users tester = new Users();
        tester.setUsername("tester");
        tester.setPassword("password123");
        tester.setEmail("reporter@example.com");
        tester.setAdmin(false);
        tester = usersRepository.save(tester);

        Users developer = new Users();
        developer.setUsername("developer");
        developer.setPassword("password123");
        developer.setEmail("developer@example.com");
        developer.setAdmin(false);
        developer = usersRepository.save(developer);

        Project project = new Project();
        project.setName("Project");
        project.setStartDate(new Date());
        project.setEndDate(new Date());
        project.setDescription("Project Description");
        project = projectRepository.save(project);

        Member member = new Member(developer, project, UserType.Developer);
        memberRepository.save(member);

        Milestone milestone = new Milestone();
        milestone.setName("Milestone 1");
        milestone.setStartDate(LocalDate.now());
        milestone.setDueDate(LocalDate.now().plusDays(30));
        milestone.setDescription("Milestone Description");
        milestone = milestoneRepository.save(milestone);

        // Given: 티켓 생성 요청
        CreateTicketDto createTicketDto = new CreateTicketDto();
        createTicketDto.setMilestone(milestone);
        createTicketDto.setReporter(tester);
        createTicketDto.setStatus(Status.New);
        createTicketDto.setPriority(Priority.Major);
        createTicketDto.setCreatedTime(LocalDateTime.now());
        createTicketDto.setModifiedTime(LocalDateTime.now());
        createTicketDto.setComponent(Component.UIComponent);
        createTicketDto.setDescription("Ticket Description");
        createTicketDto.setTitle("Ticket Title");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // JavaTimeModule 등록
        String json = objectMapper.writeValueAsString(createTicketDto);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/create/ticket/{projectId}", project.getProjectId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        List<Ticket> tickets = ticketRepository.findAll();
        Assertions.assertEquals(1, tickets.size());
        Ticket ticket = tickets.get(0);
        Assertions.assertEquals("Ticket Title", ticket.getTitle());
        Assertions.assertEquals("Ticket Description", ticket.getDescription());
        Assertions.assertEquals(Component.UIComponent, ticket.getComponent());
        Assertions.assertEquals(Priority.Major, ticket.getPriority());
        Assertions.assertEquals(Status.New, ticket.getStatus());
        Assertions.assertEquals(milestone.getMilestoneId(), ticket.getMilestone().getMilestoneId());
        Assertions.assertEquals(tester.getUserId(), ticket.getReporter().getUserId());
        Assertions.assertEquals(developer.getUserId(), ticket.getDeveloper().getUserId());
    }


}