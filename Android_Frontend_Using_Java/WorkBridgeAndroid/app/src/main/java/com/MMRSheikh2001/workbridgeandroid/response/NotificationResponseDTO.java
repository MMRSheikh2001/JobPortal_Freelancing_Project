package com.MMRSheikh2001.workbridgeandroid.response;

import com.MMRSheikh2001.workbridgeandroid.enums.NotificationType;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class NotificationResponseDTO {
    private Long id;
    private Long userId;
    private String userName;


    private String title;
    private String message;
    private NotificationType type;

    private Long referenceId;

    private Boolean isRead;
    private LocalDateTime createdAt;

}