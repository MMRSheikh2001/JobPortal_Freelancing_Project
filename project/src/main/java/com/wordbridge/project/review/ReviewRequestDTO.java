package com.wordbridge.project.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewRequestDTO {

    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;

    private String comment;

    private Long gigOrderId;


}
