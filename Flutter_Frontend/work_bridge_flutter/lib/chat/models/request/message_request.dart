import 'dart:convert';

class MessageRequestDTO {
  final String? messageText;
  final int? conversationId;

  const MessageRequestDTO({
    this.messageText,
    this.conversationId,
  });

  /// Factory constructor to create [MessageRequestDTO] from a JSON map.
  factory MessageRequestDTO.fromJson(Map<String, dynamic> json) {
    return MessageRequestDTO(
      messageText: json['messageText'] as String?,
      conversationId: (json['conversationId'] as num?)?.toInt(),
    );
  }

  /// Converts this [MessageRequestDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (messageText != null) 'messageText': messageText,
      if (conversationId != null) 'conversationId': conversationId,
    };
  }

  /// Optional helper to convert directly to a JSON string
  String toJsonString() => json.encode(toJson());

  /// Helper method to create a modified copy of this object.
  MessageRequestDTO copyWith({
    String? messageText,
    int? conversationId,
  }) {
    return MessageRequestDTO(
      messageText: messageText ?? this.messageText,
      conversationId: conversationId ?? this.conversationId,
    );
  }
}