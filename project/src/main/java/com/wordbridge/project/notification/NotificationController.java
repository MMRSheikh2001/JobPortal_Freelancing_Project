package com.wordbridge.project.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications/")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    // User notifications
    @GetMapping("user/{userId}")
    public ResponseEntity<List<NotificationResponseDTO>> getUserNotifications(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                notificationService.getUserNotifications(userId));
    }

    @GetMapping("{notificationId}")
    public ResponseEntity<NotificationResponseDTO> getById(
            @PathVariable Long notificationId) {

        return ResponseEntity.ok(notificationService.getById(notificationId));
    }


    // Unread notifications
    @GetMapping("user/{userId}/unread")
    public ResponseEntity<List<NotificationResponseDTO>> getUnreadNotifications(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                notificationService.getUnreadNotifications(userId));
    }

    // Unread count
    @GetMapping("user/{userId}/count")
    public ResponseEntity<Long> getUnreadCount(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                notificationService.getUnreadCount(userId));
    }

    // Mark one as read
    @PutMapping("{notificationId}/read")
    public ResponseEntity<NotificationResponseDTO> markAsRead(
            @PathVariable Long notificationId,
            @RequestParam Long userId) {

        return ResponseEntity.ok(
                notificationService.markAsRead(notificationId, userId));
    }

    // Mark all as read
    @PutMapping("user/{userId}/read-all")
    public ResponseEntity<Void> markAllAsRead(
            @PathVariable Long userId) {

        notificationService.markAllAsRead(userId);

        return ResponseEntity.ok().build();
    }

    // Filter by type
    @GetMapping("user/{userId}/type/{type}")
    public ResponseEntity<List<NotificationResponseDTO>> getByType(
            @PathVariable Long userId,
            @PathVariable NotificationType type) {

        return ResponseEntity.ok(
                notificationService.getNotificationsByType(userId, type));
    }

    // Admin
    @GetMapping("admin")
    public ResponseEntity<List<NotificationResponseDTO>> getAllNotifications() {

        return ResponseEntity.ok(
                notificationService.getAllNotifications());
    }

    @DeleteMapping("{notificationId}/clear")
    public ResponseEntity<Void> delete(
            @PathVariable Long notificationId,
            @RequestParam Long userId) {

        notificationService.delete(notificationId, userId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("user/{userId}/clear")
    public ResponseEntity<Void> deleteAll(

            @PathVariable Long userId) {

        notificationService.deleteAll(userId);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("search")
    public ResponseEntity<List<NotificationResponseDTO>> search(
            @RequestBody NotificationFilterDTO filter
    ) {

        return ResponseEntity.ok(
                notificationService.search(filter)
        );

    }

}
