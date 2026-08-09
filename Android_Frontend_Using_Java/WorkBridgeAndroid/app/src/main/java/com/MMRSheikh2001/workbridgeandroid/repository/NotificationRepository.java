package com.MMRSheikh2001.workbridgeandroid.repository;

import android.content.Context;

import com.MMRSheikh2001.workbridgeandroid.api.ApiClient;
import com.MMRSheikh2001.workbridgeandroid.api.ApiService;
import com.MMRSheikh2001.workbridgeandroid.enums.NotificationType;
import com.MMRSheikh2001.workbridgeandroid.request.NotificationFilterDTO;
import com.MMRSheikh2001.workbridgeandroid.response.NotificationResponseDTO;

import java.util.List;

import retrofit2.Callback;

public class NotificationRepository {


    private final ApiService apiService;

    public NotificationRepository(Context context) {
        apiService = ApiClient.getClient(context);
    }

    //==========================================
    // All Notifications
    //==========================================

    public void getUserNotifications(
            Long userId,
            Callback<List<NotificationResponseDTO>> callback) {

        apiService.getUserNotifications(userId)
                .enqueue(callback);
    }

    //==========================================
    // By Id
    //==========================================

    public void getNotificationById(
            Long notificationId,
            Callback<NotificationResponseDTO> callback) {

        apiService.getNotificationById(notificationId)
                .enqueue(callback);
    }

    //==========================================
    // Unread Notifications
    //==========================================

    public void getUnreadNotifications(
            Long userId,
            Callback<List<NotificationResponseDTO>> callback) {

        apiService.getUnreadNotifications(userId)
                .enqueue(callback);
    }

    //==========================================
    // Unread Count
    //==========================================

    public void getUnreadNotificationCount(
            Long userId,
            Callback<Long> callback) {

        apiService.getUnreadNotificationCount(userId)
                .enqueue(callback);
    }

    //==========================================
    // Mark One Read
    //==========================================

    public void markNotificationAsRead(
            Long notificationId,
            Long userId,
            Callback<NotificationResponseDTO> callback) {

        apiService.markNotificationAsRead(
                notificationId,
                userId
        ).enqueue(callback);
    }

    //==========================================
    // Mark All Read
    //==========================================

    public void markAllNotificationsAsRead(
            Long userId,
            Callback<Void> callback) {

        apiService.markAllNotificationsAsRead(userId)
                .enqueue(callback);
    }

    //==========================================
    // Filter By Type
    //==========================================

    public void getNotificationsByType(
            Long userId,
            NotificationType type,
            Callback<List<NotificationResponseDTO>> callback) {

        apiService.getNotificationsByType(
                userId,
                type
        ).enqueue(callback);
    }

    //==========================================
    // Delete One
    //==========================================

    public void deleteNotification(
            Long notificationId,
            Long userId,
            Callback<Void> callback) {

        apiService.deleteNotification(
                notificationId,
                userId
        ).enqueue(callback);
    }

    //==========================================
    // Delete All
    //==========================================

    public void deleteAllNotifications(
            Long userId,
            Callback<Void> callback) {

        apiService.deleteAllNotifications(userId)
                .enqueue(callback);
    }

    //==========================================
    // Search (Admin)
    //==========================================

    public void searchNotifications(
            NotificationFilterDTO filter,
            Callback<List<NotificationResponseDTO>> callback) {

        apiService.searchNotifications(filter)
                .enqueue(callback);
    }


}
