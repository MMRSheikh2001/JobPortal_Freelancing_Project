package com.wordbridge.project.notification;

import com.wordbridge.project.entity.User;
import com.wordbridge.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final UserRepository userRepository;


    @Override
    public NotificationResponseDTO createNotification(Long receiverId, String title, String message, NotificationType type, Long referenceId) {
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("No user found"));

        Notification notification = new Notification();

        notification.setReceiver(receiver);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);
        notification.setReferenceId(referenceId);
        notification.setIsRead(false);

        return notificationMapper.toDTO(
                notificationRepository.save(notification)
        );
    }

    @Override
    public NotificationResponseDTO getById(Long notificationId) {
        Notification notification=notificationRepository.findById(notificationId)
                .orElseThrow(()->new RuntimeException("No notification found"));

        return notificationMapper.toDTO(notification);
    }

    @Override
    public List<NotificationResponseDTO> getUserNotifications(Long userId) {
        return notificationRepository
                .findByReceiverIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(notificationMapper::toDTO)
                .toList();
    }

    @Override
    public List<NotificationResponseDTO> getUnreadNotifications(Long userId) {
        return notificationRepository
                .findByReceiverIdAndIsReadFalseOrderByCreatedAtDesc(userId)
                .stream()
                .map(notificationMapper::toDTO)
                .toList();
    }

    @Override
    public long getUnreadCount(Long userId) {
        return notificationRepository
                .countByReceiverIdAndIsReadFalse(userId);
    }

    @Override
    public NotificationResponseDTO markAsRead(Long notificationId, Long userId) {
        Notification notification = notificationRepository
                .findByIdAndReceiverId(notificationId, userId);

        if (notification == null) {
            throw new RuntimeException("Notification not found");
        }

        notification.setIsRead(true);

        return notificationMapper.toDTO(
                notificationRepository.save(notification)
        );
    }

    @Override
    public void markAllAsRead(Long userId) {
        List<Notification> notifications =
                notificationRepository.findByReceiverIdAndIsReadFalseOrderByCreatedAtDesc(userId);

        notifications.forEach(n -> n.setIsRead(true));

        notificationRepository.saveAll(notifications);
    }

    @Override
    public List<NotificationResponseDTO> getNotificationsByType(Long userId, NotificationType type) {

        return notificationRepository
                .findByReceiverIdAndTypeOrderByCreatedAtDesc(userId, type)
                .stream()
                .map(notificationMapper::toDTO)
                .toList();
    }

    @Override
    public List<NotificationResponseDTO> getAllNotifications() {

        return notificationRepository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .map(notificationMapper::toDTO)
                .toList();
    }

    @Override
    public void delete(Long notificationId, Long userId) {
        Notification notification = notificationRepository
                .findByIdAndReceiverId(notificationId, userId);

        notificationRepository.delete(notification);
    }

    @Override
    public void deleteAll(Long userId) {
        List<Notification> notifications = notificationRepository
                .findByReceiverIdOrderByCreatedAtDesc(userId);

        for (Notification notification : notifications) {
            notificationRepository.delete(notification);
        }
    }

    @Override
    public List<NotificationResponseDTO> search(
            NotificationFilterDTO filter
    ) {

        return notificationRepository
                .findAll(
                        NotificationSpecification.filter(filter)
                )
                .stream()
                .map(notificationMapper::toDTO)
                .toList();

    }
}
