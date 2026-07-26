package com.wordbridge.project.ai.dto;

import lombok.Data;

import java.util.List;

@Data
public class ResponseContent {
    private List<ResponsePart> parts;
}
