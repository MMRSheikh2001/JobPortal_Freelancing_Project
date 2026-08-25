class MessageResponseDTO {
  final int? id;
  final String? messageText;
  final String? attachment;
  final bool? isRead;
  final DateTime? sentAt;
  final int? senderId;
  final String? senderName;
  final int? conversationId;

  const MessageResponseDTO({
    this.id,
    this.messageText,
    this.attachment,
    this.isRead,
    this.sentAt,
    this.senderId,
    this.senderName,
    this.conversationId,
  });

  /// Factory constructor to create [MessageResponseDTO] from a JSON map.
  factory MessageResponseDTO.fromJson(Map<String, dynamic> json) {
    return MessageResponseDTO(
      id: (json['id'] as num?)?.toInt(),
      messageText: json['messageText'] as String?,
      attachment: json['attachment'] as String?,
      isRead: json['isRead'] as bool?,
      sentAt: json['sentAt'] != null
          ? DateTime.tryParse(json['sentAt'] as String)
          : null,
      senderId: (json['senderId'] as num?)?.toInt(),
      senderName: json['senderName'] as String?,
      conversationId: (json['conversationId'] as num?)?.toInt(),
    );
  }

  /// Converts this [MessageResponseDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (id != null) 'id': id,
      if (messageText != null) 'messageText': messageText,
      if (attachment != null) 'attachment': attachment,
      if (isRead != null) 'isRead': isRead,
      if (sentAt != null) 'sentAt': sentAt!.toIso8601String(),
      if (senderId != null) 'senderId': senderId,
      if (senderName != null) 'senderName': senderName,
      if (conversationId != null) 'conversationId': conversationId,
    };
  }

  /// Helper method to create a modified copy of this object.
  MessageResponseDTO copyWith({
    int? id,
    String? messageText,
    String? attachment,
    bool? isRead,
    DateTime? sentAt,
    int? senderId,
    String? senderName,
    int? conversationId,
  }) {
    return MessageResponseDTO(
      id: id ?? this.id,
      messageText: messageText ?? this.messageText,
      attachment: attachment ?? this.attachment,
      isRead: isRead ?? this.isRead,
      sentAt: sentAt ?? this.sentAt,
      senderId: senderId ?? this.senderId,
      senderName: senderName ?? this.senderName,
      conversationId: conversationId ?? this.conversationId,
    );
  }
}