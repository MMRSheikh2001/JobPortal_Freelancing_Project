import 'package:work_bridge_flutter/enums/notification_type.dart';

class NotificationFilterDTO {
  final NotificationType? type;
  final bool? isRead;
  final int? userId;
  final String? keyword;

  const NotificationFilterDTO({
    this.type,
    this.isRead,
    this.userId,
    this.keyword,
  });

  /// Factory constructor to create [NotificationFilterDTO] from a JSON map.
  factory NotificationFilterDTO.fromJson(Map<String, dynamic> json) {
    return NotificationFilterDTO(
      type: json['type'] != null
          ? NotificationType.fromJson(json['type'] as String)
          : null,
      isRead: json['isRead'] as bool?,
      userId: (json['userId'] as num?)?.toInt(),
      keyword: json['keyword'] as String?,
    );
  }

  /// Converts this [NotificationFilterDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (type != null) 'type': type!.toJson(),
      if (isRead != null) 'isRead': isRead,
      if (userId != null) 'userId': userId,
      if (keyword != null) 'keyword': keyword,
    };
  }

  /// Helper method to create a modified copy of this object.
  NotificationFilterDTO copyWith({
    NotificationType? type,
    bool? isRead,
    int? userId,
    String? keyword,
  }) {
    return NotificationFilterDTO(
      type: type ?? this.type,
      isRead: isRead ?? this.isRead,
      userId: userId ?? this.userId,
      keyword: keyword ?? this.keyword,
    );
  }
}