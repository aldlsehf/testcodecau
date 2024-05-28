package com.cau.swtestcode.domain;

import com.cau.swtestcode.domain.enumClass.UserType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Member")
@Getter
@Setter
@NoArgsConstructor
public class Member {
    //4 attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private Users user;

    // user have different type per project
    @Enumerated(EnumType.STRING)
    private UserType userType;
}
