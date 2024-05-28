package com.cau.swtestcode.dto.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
@Data
public class CreateProjectReq {

    @NotBlank(message = "Project name is required")
    private String name;

    @NotNull(message = "Start date is required")
    private Date startDate;

    @NotNull(message = "End date is required")
    private Date endDate;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "User accounts list is required")
    private List<ManageUserAccountReq> manageUserAccounts;
}
