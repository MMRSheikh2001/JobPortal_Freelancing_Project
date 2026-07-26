package com.wordbridge.project.ai;

import lombok.Data;

@Data
public class AIInterviewQuestionResponseDTO {
    private Long id;

    private String question;

    private String answer;

    private Integer score;

}
