package com.wordbridge.project.ai;

import lombok.Data;

import java.util.List;

@Data
public class InterviewQuestionResult {
    private Integer totalScore;
    private List<InterviewQuestion> questions;
}
