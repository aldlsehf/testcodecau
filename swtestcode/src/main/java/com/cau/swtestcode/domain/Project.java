package com.cau.swtestcode.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Project")
@Getter
@Setter
@NoArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    // @CreationTimestamp //proj created times != start time
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    private Date endDate;

    @JsonIgnore
    @OneToMany(mappedBy = "project")
    private List<Member> members;

    public static Project saveProject(String name, String description, Date startDate, Date endDate) {
        Project project = new Project();
        project.setName(name);
        project.setStartDate(startDate);
        project.setEndDate(endDate);
        project.setDescription(description);

        return project;
    }
}
