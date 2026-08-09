package com.wordbridge.project.notification;

import lombok.Data;

@Data
public class NotificationFilterDTO {




    private NotificationType type;

    private Boolean isRead;

    private Long userId;

    private String keyword;
}
