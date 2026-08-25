import 'package:work_bridge_flutter/enums/notification_type.dart';

class NotificationResponseDTO {
  final int? id;
  final int? userId;
  final String? userName;
  final String? title;
  final String? message;
  final NotificationType? type;
  final int? referenceId;
  final bool? isRead;
  final DateTime? createdAt;

  const NotificationResponseDTO({
    this.id,
    this.userId,
    this.userName,
    this.title,
    this.message,
    this.type,
    this.referenceId,
    this.isRead,
    this.createdAt,
  });

  /// Factory constructor to create [NotificationResponseDTO] from a JSON map.
  factory NotificationResponseDTO.fromJson(Map<String, dynamic> json) {
    return NotificationResponseDTO(
      id: (json['id'] as num?)?.toInt(),
      userId: (json['userId'] as num?)?.toInt(),
      userName: json['userName'] as String?,
      title: json['title'] as String?,
      message: json['message'] as String?,
      type: json['type'] != null
          ? NotificationType.fromJson(json['type'] as String)
          : null,
      referenceId: (json['referenceId'] as num?)?.toInt(),
      isRead: json['isRead'] as bool?,
      createdAt: json['createdAt'] != null
          ? DateTime.tryParse(json['createdAt'] as String)
          : null,
    );
  }

  /// Converts this [NotificationResponseDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (id != null) 'id': id,
      if (userId != null) 'userId': userId,
      if (userName != null) 'userName': userName,
      if (title != null) 'title': title,
      if (message != null) 'message': message,
      if (type != null) 'type': type!.toJson(),
      if (referenceId != null) 'referenceId': referenceId,
      if (isRead != null) 'isRead': isRead,
      if (createdAt != null) 'createdAt': createdAt!.toIso8601String(),
    };
  }

  /// Helper method to create a modified copy of this object.
  NotificationResponseDTO copyWith({
    int? id,
    int? userId,
    String? userName,
    String? title,
    String? message,
    NotificationType? type,
    int? referenceId,
    bool? isRead,
    DateTime? createdAt,
  }) {
    return NotificationResponseDTO(
      id: id ?? this.id,
      userId: userId ?? this.userId,
      userName: userName ?? this.userName,
      title: title ?? this.title,
      message: message ?? this.message,
      type: type ?? this.type,
      referenceId: referenceId ?? this.referenceId,
      isRead: isRead ?? this.isRead,
      createdAt: createdAt ?? this.createdAt,
    );
  }
}
