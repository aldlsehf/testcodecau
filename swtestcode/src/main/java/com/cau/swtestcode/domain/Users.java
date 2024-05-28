package com.cau.swtestcode.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Users")
@Getter
@Setter
@NoArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String username;

    private String password;

    private String email;

    private boolean admin;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Member> members;

    @JsonIgnore
    @OneToMany(mappedBy = "reporter")
    private List<Ticket> reportedTickets;

    @JsonIgnore
    @OneToMany(mappedBy = "developer")
    private List<Ticket> assignedTickets;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Comment> comments;


}