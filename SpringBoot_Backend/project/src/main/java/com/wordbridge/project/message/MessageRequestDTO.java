package com.wordbridge.project.message;

import lombok.Data;

@Data
public class MessageRequestDTO {

    private String messageText;

    private Long conversationId;

}
