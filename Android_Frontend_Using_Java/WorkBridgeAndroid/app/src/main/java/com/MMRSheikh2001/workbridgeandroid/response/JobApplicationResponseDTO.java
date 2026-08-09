package com.MMRSheikh2001.workbridgeandroid.response;

import com.MMRSheikh2001.workbridgeandroid.enums.ApplicationStatus;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class JobApplicationResponseDTO {

    private Long id;


    private ApplicationStatus status;

    private LocalDateTime appliedAt;
    private LocalDateTime aiDeadlineDate;



    private String companyNotes;

    private Long jobId;

    private String jobTitle;
    private String jobDescription;

    private Long companyProfileId;
    private String companyName;

    private Long companyUserId;
    private String companyUserEmail;
    private String companyLogo;


    private Long userProfileId;
    private String userName;
    private String userImage;

    private Long userId;
    private String userEmail;

    //AI Completed
    private Integer aiMatchScore;


    private String aiMatchFeedback;

    private Integer aiInterviewScore;

    private Integer aiFinalScore;

    private Boolean aiInterviewCompleted;

    private LocalDateTime aiCompletedAt;

    private Boolean aiShortlisted;



    //AI Integration
    private Boolean aiScreeningEnabled;

    private Boolean aiCvScreeningEnabled;

    private Boolean aiInterviewEnabled;

    private Integer aiMatchThreshold;

    private Integer aiQuestionCount;



}
