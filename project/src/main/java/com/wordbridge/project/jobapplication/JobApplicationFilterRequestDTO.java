package com.wordbridge.project.jobapplication;

import com.wordbridge.project.enums.ApplicationStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class JobApplicationFilterRequestDTO {

    private String keyword;

    private Long companyProfileId;

    private Long jobId;

    private Long userProfileId;

    private Long categoryId;

    private ApplicationStatus status;

    private Boolean aiCompleted;

    private Boolean aiShortlisted;

    private LocalDate appliedFrom;

    private LocalDate appliedTo;

}
