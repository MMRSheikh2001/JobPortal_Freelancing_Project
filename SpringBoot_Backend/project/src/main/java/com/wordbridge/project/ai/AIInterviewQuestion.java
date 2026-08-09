package com.wordbridge.project.ai;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "aiinterviewquestions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIInterviewQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private AIInterviewSession session;


    @Lob
    @Column(columnDefinition = "TEXT")
    private String question;


    @Lob
    @Column(columnDefinition = "TEXT")
    private String answer;

    private Integer score;




}
