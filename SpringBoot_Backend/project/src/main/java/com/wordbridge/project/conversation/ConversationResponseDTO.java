package com.wordbridge.project.conversation;

import com.wordbridge.project.enums.ConversationStatus;
import com.wordbridge.project.enums.GigOrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConversationResponseDTO {
    private Long id;


    private LocalDateTime createdAt;

    private LocalDateTime lastMessageAt;

    private Long gigOrderId;
    private GigOrderStatus status;

    private Long gigId;
    private String gigTitle;
    private String gigImage;

    private Long sellerUserProfileId;
    private String sellerName;

    private Long buyerId;
    private String buyerName;

    private ConversationStatus conversationStatus;




}
