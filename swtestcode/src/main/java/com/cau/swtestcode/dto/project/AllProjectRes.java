package com.cau.swtestcode.dto.project;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
@Data
public class AllProjectRes {

    private List<ProjectRes> projectList;

}
