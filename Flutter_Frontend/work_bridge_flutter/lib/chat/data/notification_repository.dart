import 'package:dio/dio.dart';
import 'package:work_bridge_flutter/chat/models/request/notification_filter.dart';
import 'package:work_bridge_flutter/chat/models/response/notification_response.dart';
import 'package:work_bridge_flutter/enums/notification_type.dart';
import 'package:work_bridge_flutter/utils/api_client.dart';
import 'package:work_bridge_flutter/utils/api_constants.dart';

class NotificationRepository {
  NotificationRepository(this._apiClient);

  final ApiClient _apiClient;

  Dio get _dio => _apiClient.dio;

  Future<List<NotificationResponseDTO>> getUserNotifications(int userId) async {
    final response = await _dio.get(ApiConstants.userNotifications(userId));

    return (response.data as List)
        .map((json) => NotificationResponseDTO.fromJson(json))
        .toList();
  }

  Future<NotificationResponseDTO> getNotificationById(
    int notificationId,
  ) async {
    final response = await _dio.get(
      ApiConstants.notificationById(notificationId),
    );

    return NotificationResponseDTO.fromJson(response.data);
  }

  Future<List<NotificationResponseDTO>> getUnreadNotifications(
    int userId,
  ) async {
    final response = await _dio.get(ApiConstants.unreadNotifications(userId));

    return (response.data as List)
        .map((json) => NotificationResponseDTO.fromJson(json))
        .toList();
  }

  Future<int> getUnreadNotificationCount(int userId) async {
    final response = await _dio.get(
      ApiConstants.unreadNotificationCount(userId),
    );

    return (response.data as num).toInt();
  }

  Future<NotificationResponseDTO> markNotificationAsRead(
    int notificationId,
    int userId,
  ) async {
    final response = await _dio.put(
      ApiConstants.markNotificationRead(notificationId),
      queryParameters: {'userId': userId},
    );

    return NotificationResponseDTO.fromJson(response.data);
  }

  Future<void> markAllNotificationsAsRead(int userId) async {
    await _dio.put(ApiConstants.markAllNotificationsRead(userId));
  }

  Future<List<NotificationResponseDTO>> getNotificationsByType(
    int userId,
    NotificationType type,
  ) async {
    final response = await _dio.get(
      ApiConstants.notificationsByType(userId, type.toJson()),
    );

    return (response.data as List)
        .map((json) => NotificationResponseDTO.fromJson(json))
        .toList();
  }

  Future<void> deleteNotification(int notificationId, int userId) async {
    await _dio.delete(
      ApiConstants.deleteNotification(notificationId),
      queryParameters: {'userId': userId},
    );
  }

  Future<void> deleteAllNotifications(int userId) async {
    await _dio.delete(ApiConstants.deleteAllNotifications(userId));
  }

  Future<List<NotificationResponseDTO>> searchNotifications(
    NotificationFilterDTO filter,
  ) async {
    final response = await _dio.post(
      ApiConstants.searchNotifications,
      data: filter.toJson(),
    );

    return (response.data as List)
        .map((json) => NotificationResponseDTO.fromJson(json))
        .toList();
  }
}
