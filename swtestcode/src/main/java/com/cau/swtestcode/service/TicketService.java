package com.cau.swtestcode.service;


import com.cau.swtestcode.domain.Member;
import com.cau.swtestcode.domain.Project;
import com.cau.swtestcode.domain.Ticket;
import com.cau.swtestcode.domain.Users;
import com.cau.swtestcode.domain.enumClass.Priority;
import com.cau.swtestcode.domain.enumClass.Status;
import com.cau.swtestcode.domain.enumClass.UserType;
import com.cau.swtestcode.dto.ticket.request.CreateTicketDto;
import com.cau.swtestcode.dto.ticket.request.UpdateStatusReq;
import com.cau.swtestcode.dto.ticket.response.AssignedTicketListRes;
import com.cau.swtestcode.dto.ticket.response.DetailTicketRes;
import com.cau.swtestcode.dto.ticket.response.StaticsRes;
import com.cau.swtestcode.dto.ticket.response.TicketListRes;
import com.cau.swtestcode.repository.MemberRepository;
import com.cau.swtestcode.repository.ProjectRepository;
import com.cau.swtestcode.repository.TicketRepository;
import com.cau.swtestcode.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ProjectRepository projectRepository;


    private static final EnumSet<Status> PROJECT_LEADER_STATUSES = EnumSet.of(Status.Assigned, Status.Closed);
    private static final EnumSet<Status> DEVELOPER_STATUSES = EnumSet.of(Status.Resolved);
    private static final EnumSet<Status> TESTER_STATUSES = EnumSet.of(Status.New, Status.Reopened);

    @Transactional
    public void createTicket(Long projectId, CreateTicketDto ticketDTO) {
        Ticket ticket = new Ticket();

        // automatically assign time when created -(오슬희)
        LocalDateTime now = LocalDateTime.now();

        ticket.setMilestone(ticketDTO.getMilestone());
        ticket.setReporter(ticketDTO.getReporter());
        ticket.setStatus(Status.New); // always set to New when created -(오슬희) : Because it is a new! create ticket
        ticket.setPriority(ticketDTO.getPriority());
        ticket.setCreatedTime(now);
        ticket.setModifiedTime(now);
        ticket.setComponent(ticketDTO.getComponent());
        ticket.setDescription(ticketDTO.getDescription());
        ticket.setTitle(ticketDTO.getTitle());

        //here is the algorithm to automatically assign developer to the ticket -(오슬희)
        // algorithm step1: get all developers in the project -(오슬희)
        // algorithm step2: get the developer with tickets assigned number info ? -(오슬희) - 개발자 별로 할당된 티켓 수를 구하란 뜻임 영어가 안되서 미안
        //algorithm step3: if number info is same, compare the number of tickets assigned -(오슬희)
        Project project = projectRepository.findByProjectId(projectId);

        // get developers in this project -(오슬희)
        // get the developer with the least number of tickets assigned -(오슬희)
        List<Member> developers = memberRepository.findAllByProjectAndUserType(project, UserType.Developer); // change optioal<user> to mapping -(오슬희)
        Optional<Users> assignedDeveloper = developers.stream()
                .collect(Collectors.groupingBy(Member::getUser, Collectors.summingInt(dev -> (int) ticketRepository.countByComponentAndDeveloper(ticketDTO.getComponent(), dev.getUser()))))
                .entrySet().stream()
                .sorted((entry1, entry2) -> {
                    int cmp = entry2.getValue().compareTo(entry1.getValue());
                    if (cmp == 0) {
                        // 동일한 티켓 수일 경우, 할당된 티켓 수로 비교-(오슬희)
                        long dev1AssignedCount = ticketRepository.countByDeveloperAndStatus(entry1.getKey(), Status.Assigned);
                        long dev2AssignedCount = ticketRepository.countByDeveloperAndStatus(entry2.getKey(), Status.Assigned);
                        cmp = Long.compare(dev1AssignedCount, dev2AssignedCount);
                    }
                    if (cmp == 0) {
                        // 동일한 할당된 티켓 수일 경우, 사용자 ID로 비교-(오슬희)
                        cmp = entry1.getKey().getUserId().compareTo(entry2.getKey().getUserId());
                    }
                    return cmp;
                })
                .map(entry -> entry.getKey())
                .findFirst();

        assignedDeveloper.ifPresent(ticket::setDeveloper);
        Ticket savedTicket = ticketRepository.save(ticket);// you can save and delete variable (if you want to 반환 use this variable: savedTicket) - (오슬희)

    }

    @Transactional(readOnly = true)
    public TicketListRes readTicketList(Long projectId, Long userId) {
        // join MemberTable to check usertype (-오슬희)
        Member member = memberRepository.findByProject_ProjectIdAndUser_UserId(projectId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));
//
//        if ((member.getUserType() != UserType.Tester)||(member.getUserType() != UserType.Developer)) {
//            throw new IllegalArgumentException("해당 사용자는 개발자가가 아닙니다.");
//        }

// if you read this code, 갗이 고민좀? -(오슬희) 개발자 입장에서 짠거라 약간 어색함

        UserType actor = member.getUserType();

        List<TicketListRes.TicketInfo> tickets = switch (actor) {
            case ProjectLeader -> ticketRepository.findByProject_ProjectId(projectId)
                    .stream()
                    .map(ticket -> new TicketListRes.TicketInfo(
                            ticket.getTitle(),
                            ticket.getStatus().toString(),
                            ticket.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    ))
                    .collect(Collectors.toList());
            case Developer -> ticketRepository.findByProject_ProjectIdAndDeveloper_UserId(projectId, userId)
                    .stream()
                    .map(ticket -> new TicketListRes.TicketInfo(
                            ticket.getTitle(),
                            ticket.getStatus().toString(),
                            ticket.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    ))
                    .collect(Collectors.toList());
            case Tester -> ticketRepository.findByProject_ProjectIdAndDeveloper_UserId(projectId, userId)
                    .stream()
                    .map(ticket -> new TicketListRes.TicketInfo(
                            ticket.getTitle(),
                            ticket.getStatus().toString(),
                            ticket.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    ))
                    .collect(Collectors.toList());
        };


        // Get: Assigned ticket List - (오슬희)
        List<TicketListRes.TicketInfo> assignedTickets =
                ticketRepository.findByProject_ProjectIdAndDeveloper_UserIdAndStatus(projectId, userId, Status.Assigned)
                        .stream()
                        .map(ticket -> new TicketListRes.TicketInfo(
                                ticket.getTitle(),
                                ticket.getStatus().toString(),
                                ticket.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        ))
                        .collect(Collectors.toList());

        // Get: Closed ticket List -(오슬희)
        List<TicketListRes.TicketInfo> closedTickets =
                ticketRepository.findByProject_ProjectIdAndDeveloper_UserIdAndStatus(projectId, userId, Status.Closed)
                        .stream()
                        .map(ticket -> new TicketListRes.TicketInfo(
                                ticket.getTitle(),
                                ticket.getStatus().toString(),
                                ticket.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        ))
                        .collect(Collectors.toList());

        return new TicketListRes(assignedTickets, closedTickets);
    }

    @Transactional(readOnly = true)
    public List<AssignedTicketListRes> readAssignedTicketList(Long projectId, Long userId) {
        Member member = memberRepository.findByProject_ProjectIdAndUser_UserId(projectId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        UserType userType = member.getUserType();
        if (userType != UserType.Developer && userType != UserType.ProjectLeader) {
            throw new IllegalArgumentException("해당 사용자는 권한이 없습니다.");
        }

        List<Ticket> assignedTickets = ticketRepository.findByProject_ProjectIdAndStatus(projectId, Status.Assigned);

        return assignedTickets.stream()
                .map(ticket -> new AssignedTicketListRes(
                        ticket.getTitle(),
                        ticket.getPriority(),
                        ticket.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public DetailTicketRes readDetailTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("해당 티켓을 찾을 수 없습니다. ID: " + ticketId));

        List<DetailTicketRes.CommentResponse> comments = ticket.getComments().stream()
                .map(comment -> new DetailTicketRes.CommentResponse(
                        comment.getContent(),
                        comment.getTimeStamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                ))
                .collect(Collectors.toList());

        return new DetailTicketRes(
                ticket.getDescription(),
                ticket.getStatus().toString(),
                ticket.getPriority().toString(),
                ticket.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                ticket.getModifiedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                ticket.getComponent().toString(),
                ticket.getDeveloper().getUsername(),
                ticket.getReporter().getUsername(),
                ticket.getMilestone().getName(),
                comments
        );
    }

    @Transactional(readOnly = true)
    public StaticsRes readTicketStatics(Long projectId) {
        //read ticket that created today and this month per List -(오슬희)
        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        LocalDateTime endOfToday = startOfToday.plusDays(1);
        //get this month info from now()-(오슬희)
        YearMonth thisMonth = YearMonth.now();
        LocalDateTime startOfMonth = thisMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = thisMonth.atEndOfMonth().atTime(23, 59, 59);

        List<Ticket> todayTickets = ticketRepository.findByProject_ProjectIdAndCreatedTimeBetween(projectId, startOfToday, endOfToday);
        List<Ticket> monthTickets = ticketRepository.findByProject_ProjectIdAndCreatedTimeBetween(projectId, startOfMonth, endOfMonth);

        Map<Status, Long> todayStatusCount = todayTickets.stream().collect(Collectors.groupingBy(Ticket::getStatus, Collectors.counting()));
        Map<Priority, Long> todayPriorityCount = todayTickets.stream().collect(Collectors.groupingBy(Ticket::getPriority, Collectors.counting()));

        Map<Status, Long> monthStatusCount = monthTickets.stream().collect(Collectors.groupingBy(Ticket::getStatus, Collectors.counting()));
        Map<Priority, Long> monthPriorityCount = monthTickets.stream().collect(Collectors.groupingBy(Ticket::getPriority, Collectors.counting()));

        return new StaticsRes(todayStatusCount, todayPriorityCount, monthStatusCount, monthPriorityCount);
    }

    @Transactional
    public String updateTicketStatus(Long projectId, Long ticketId, Long userId, UpdateStatusReq dto) {
        Member member = memberRepository.findByProject_ProjectIdAndUser_UserId(projectId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));
        //have to join member table to get user type so, you can check the user Authority -(오슬희)
        UserType userType = member.getUserType();
        Status newStatus = dto.getStatus();

        boolean isStatusChangeAllowed = switch (userType) {
            case ProjectLeader -> PROJECT_LEADER_STATUSES.contains(newStatus);
            case Developer -> DEVELOPER_STATUSES.contains(newStatus);
            case Tester -> TESTER_STATUSES.contains(newStatus);
        };

        if (!isStatusChangeAllowed) {
            return "권한이 없습니다.";
        }

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("해당 티켓을 찾을 수 없습니다."));

        ticket.setStatus(newStatus);
        ticketRepository.save(ticket);

        return "상태 변경 성공";
    }

    @Transactional
    public void deleteTicket(Long ticketId) {
        ticketRepository.deleteById(ticketId);
    }


}
