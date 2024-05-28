package com.cau.swtestcode.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Milestone")
@Getter
@Setter
@NoArgsConstructor
public class Milestone {
    //6 attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long milestoneId;

    private String name;

    private LocalDate startDate;
    //LocalDate: mapped DATE tpe in DB
    private LocalDate dueDate;

    @Column(columnDefinition = "TEXT")
    private String description;


    @OneToMany(mappedBy = "milestone")
    private List<Ticket> tickets;
}
