package com.wordbridge.project.ai;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wordbridge.project.jobapplication.JobApplication;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "aiinterviewsessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIInterviewSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private JobApplication application;

    private LocalDateTime startedAt;



    private LocalDateTime completedAt;

    private Integer totalScore;

    private Boolean interviewCompleted;

    @OneToMany
    @JsonIgnore
    private List<AIInterviewQuestion> aiInterviewQuestions= new ArrayList<>();


}
