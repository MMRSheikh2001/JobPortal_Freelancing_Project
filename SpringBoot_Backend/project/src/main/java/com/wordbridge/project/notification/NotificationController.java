package com.wordbridge.project.notification;

import com.wordbridge.project.entity.User;
import com.wordbridge.project.enums.UserRole;
import com.wordbridge.project.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications/")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final AuthenticationService authenticationService;

    // User notifications
    @PreAuthorize("isAuthenticated()")
    @GetMapping("user/{userId}")
    public ResponseEntity<List<NotificationResponseDTO>> getUserNotifications(
            @PathVariable Long userId) {

        checkUserIdOwnership(userId);
        return ResponseEntity.ok(
                notificationService.getUserNotifications(userId));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("{notificationId}")
    public ResponseEntity<NotificationResponseDTO> getById(
            @PathVariable Long notificationId) {

        NotificationResponseDTO n = notificationService.getById(notificationId);
        checkUserIdOwnership(n.getUserId());
        return ResponseEntity.ok(n);
    }


    // Unread notifications
    @PreAuthorize("isAuthenticated()")
    @GetMapping("user/{userId}/unread")
    public ResponseEntity<List<NotificationResponseDTO>> getUnreadNotifications(
            @PathVariable Long userId) {

        checkUserIdOwnership(userId);
        return ResponseEntity.ok(
                notificationService.getUnreadNotifications(userId));
    }

    // Unread count
    @PreAuthorize("isAuthenticated()")
    @GetMapping("user/{userId}/count")
    public ResponseEntity<Long> getUnreadCount(
            @PathVariable Long userId) {

        checkUserIdOwnership(userId);
        return ResponseEntity.ok(
                notificationService.getUnreadCount(userId));
    }

    // Mark one as read
    @PreAuthorize("isAuthenticated()")
    @PutMapping("{notificationId}/read")
    public ResponseEntity<NotificationResponseDTO> markAsRead(
            @PathVariable Long notificationId,
            @RequestParam Long userId) {
        checkUserIdOwnership(userId);

        return ResponseEntity.ok(
                notificationService.markAsRead(notificationId, userId));
    }

    // Mark all as read
    @PreAuthorize("isAuthenticated()")
    @PutMapping("user/{userId}/read-all")
    public ResponseEntity<Void> markAllAsRead(
            @PathVariable Long userId) {

        checkUserIdOwnership(userId);
        notificationService.markAllAsRead(userId);

        return ResponseEntity.ok().build();
    }

    // Filter by type
    @PreAuthorize("isAuthenticated()")
    @GetMapping("user/{userId}/type/{type}")
    public ResponseEntity<List<NotificationResponseDTO>> getByType(
            @PathVariable Long userId,
            @PathVariable NotificationType type) {
        checkUserIdOwnership(userId);

        return ResponseEntity.ok(
                notificationService.getNotificationsByType(userId, type));
    }

    // Admin
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("admin")
    public ResponseEntity<List<NotificationResponseDTO>> getAllNotifications() {

        return ResponseEntity.ok(
                notificationService.getAllNotifications());
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("{notificationId}/clear")
    public ResponseEntity<Void> delete(
            @PathVariable Long notificationId,
            @RequestParam Long userId) {
        checkUserIdOwnership(userId);

        notificationService.delete(notificationId, userId);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("user/{userId}/clear")
    public ResponseEntity<Void> deleteAll(

            @PathVariable Long userId) {
        checkUserIdOwnership(userId);

        notificationService.deleteAll(userId);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("search")
    public ResponseEntity<List<NotificationResponseDTO>> search(
            @RequestBody NotificationFilterDTO filter
    ) {

        User currentUser = authenticationService.getCurrentUser();
        if (currentUser.getRole() != UserRole.ADMIN) {
            filter.setUserId(currentUser.getId());
        }
        return ResponseEntity.ok(
                notificationService.search(filter)
        );

    }

    private void checkUserIdOwnership(Long userId) {
        User currentUser = authenticationService.getCurrentUser();
        if (!currentUser.getId().equals(userId) && currentUser.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("Not allowed");
        }
    }

}
