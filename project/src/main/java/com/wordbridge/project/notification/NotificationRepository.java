package com.wordbridge.project.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long>,
        JpaSpecificationExecutor<Notification> {

    // All notifications of a user (newest first)
    List<Notification> findByReceiverIdOrderByCreatedAtDesc(Long userId);

    // Unread notifications of a user
    List<Notification> findByReceiverIdAndIsReadFalseOrderByCreatedAtDesc(Long userId);

    // Count unread notifications
    long countByReceiverIdAndIsReadFalse(Long userId);

    // Find a notification of a user
    Notification findByIdAndReceiverId(Long notificationId, Long userId);

    // Notifications by type
    List<Notification> findByReceiverIdAndTypeOrderByCreatedAtDesc(
            Long userId,
            NotificationType type
    );

    // Admin view (all notifications)
    List<Notification> findAllByOrderByCreatedAtDesc();

}
