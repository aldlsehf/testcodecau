package com.cau.swtestcode.repository;

import com.cau.swtestcode.domain.Ticket;
import com.cau.swtestcode.domain.Users;
import com.cau.swtestcode.domain.enumClass.Component;
import com.cau.swtestcode.domain.enumClass.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    long countByComponentAndDeveloper(Component component, Users developer);
    long countByDeveloperAndStatus(Users developer, Status status);
    List<Ticket> findByProject_ProjectIdAndDeveloper_UserIdAndStatus(Long projectId, Long userId, Status status);
    // used to get ticket List api
    List<Ticket> findByProject_ProjectIdAndCreatedTimeBetween(Long projectId, LocalDateTime start, LocalDateTime end);

    List<Ticket> findByProject_ProjectIdAndStatus(Long projectId, Status status);
    List<Ticket> findByProject_ProjectIdAndDeveloper_UserId(Long projectId, Long userId);
    List<Ticket> findByProject_ProjectId(Long projectId);


}