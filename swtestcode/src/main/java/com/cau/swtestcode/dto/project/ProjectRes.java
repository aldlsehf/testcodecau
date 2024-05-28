package com.cau.swtestcode.dto.project;

import lombok.Data;
import lombok.Getter;

import java.util.Date;

//projectList의 프로젝트들 in AllProjectRes
@Getter
@Data
public class ProjectRes {

    private String name;
    private Date startDate;
    private Date endDate;

}
