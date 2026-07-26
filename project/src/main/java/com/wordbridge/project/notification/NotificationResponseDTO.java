package com.wordbridge.project.notification;

import com.wordbridge.project.enums.UserRole;
import lombok.Data;

import java.time.LocalDateTime;

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
