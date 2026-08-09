package com.wordbridge.project.notification;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NotificationService {

    NotificationResponseDTO createNotification(
            Long receiverId,
            String title,
            String message,
            NotificationType type,
            Long referenceId
    );

    NotificationResponseDTO getById(Long notificationId);

    List<NotificationResponseDTO> getUserNotifications(Long userId);

    List<NotificationResponseDTO> getUnreadNotifications(Long userId);

    long getUnreadCount(Long userId);

    NotificationResponseDTO markAsRead(Long notificationId, Long userId);

    void markAllAsRead(Long userId);

    List<NotificationResponseDTO> getNotificationsByType(
            Long userId,
            NotificationType type
    );

    List<NotificationResponseDTO> getAllNotifications();

    void delete(Long notificationId, Long userId);

    void  deleteAll(Long userId);

    List<NotificationResponseDTO> search(
            NotificationFilterDTO filter
    );

}
