package com.wordbridge.project.message;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageResponseDTO {

    private Long id;
    private String messageText;
    private String attachment;

    private Boolean isRead;

    private LocalDateTime sentAt;

    private Long senderId;
    private String senderName;

    private Long conversationId;

}
