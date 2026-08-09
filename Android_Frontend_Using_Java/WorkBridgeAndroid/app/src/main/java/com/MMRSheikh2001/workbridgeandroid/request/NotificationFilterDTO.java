package com.MMRSheikh2001.workbridgeandroid.request;

import com.MMRSheikh2001.workbridgeandroid.enums.NotificationType;

import lombok.Data;

@Data
public class NotificationFilterDTO {




    private NotificationType type;

    private Boolean isRead;

    private Long userId;

    private String keyword;
}