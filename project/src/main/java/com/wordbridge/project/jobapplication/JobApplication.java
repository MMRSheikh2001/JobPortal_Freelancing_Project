package com.wordbridge.project.jobapplication;

import com.wordbridge.project.entity.UserProfile;
import com.wordbridge.project.enums.ApplicationStatus;
import com.wordbridge.project.job.Job;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "jobapplications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Job job;

    @ManyToOne
    private UserProfile userProfile;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;


    private LocalDateTime appliedAt;

    private LocalDateTime aiDeadlineDate;



    @Lob
    @Column(columnDefinition = "TEXT")
    private String companyNotes;

    //AI Completed
    private Integer aiMatchScore;

    @Column(length = 3000)
    private String aiMatchFeedback;

    private Integer aiInterviewScore;

    private Integer aiFinalScore;

    private Boolean aiInterviewCompleted;

    private LocalDateTime aiCompletedAt;

    private Boolean aiShortlisted;

}
