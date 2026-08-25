import 'package:work_bridge_flutter/enums/conversation_status.dart';
import 'package:work_bridge_flutter/enums/gig_order_status.dart';

class ConversationResponseDTO {
  final int? id;
  final DateTime? createdAt;
  final DateTime? lastMessageAt;
  final int? gigOrderId;
  final GigOrderStatus? status;
  final int? gigId;
  final String? gigTitle;
  final String? gigImage;
  final int? sellerUserProfileId;
  final String? sellerName;
  final int? buyerId;
  final String? buyerName;
  final ConversationStatus? conversationStatus;

  const ConversationResponseDTO({
    this.id,
    this.createdAt,
    this.lastMessageAt,
    this.gigOrderId,
    this.status,
    this.gigId,
    this.gigTitle,
    this.gigImage,
    this.sellerUserProfileId,
    this.sellerName,
    this.buyerId,
    this.buyerName,
    this.conversationStatus,
  });

  /// Factory constructor to create [ConversationResponseDTO] from a JSON map.
  factory ConversationResponseDTO.fromJson(Map<String, dynamic> json) {
    return ConversationResponseDTO(
      id: (json['id'] as num?)?.toInt(),
      createdAt: json['createdAt'] != null
          ? DateTime.tryParse(json['createdAt'] as String)
          : null,
      lastMessageAt: json['lastMessageAt'] != null
          ? DateTime.tryParse(json['lastMessageAt'] as String)
          : null,
      gigOrderId: (json['gigOrderId'] as num?)?.toInt(),
      status: json['status'] != null
          ? GigOrderStatus.fromJson(json['status'] as String)
          : null,
      gigId: (json['gigId'] as num?)?.toInt(),
      gigTitle: json['gigTitle'] as String?,
      gigImage: json['gigImage'] as String?,
      sellerUserProfileId: (json['sellerUserProfileId'] as num?)?.toInt(),
      sellerName: json['sellerName'] as String?,
      buyerId: (json['buyerId'] as num?)?.toInt(),
      buyerName: json['buyerName'] as String?,
      conversationStatus: json['conversationStatus'] != null
          ? ConversationStatus.fromJson(json['conversationStatus'] as String)
          : null,
    );
  }

  /// Converts this [ConversationResponseDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (id != null) 'id': id,
      if (createdAt != null) 'createdAt': createdAt!.toIso8601String(),
      if (lastMessageAt != null)
        'lastMessageAt': lastMessageAt!.toIso8601String(),
      if (gigOrderId != null) 'gigOrderId': gigOrderId,
      if (status != null) 'status': status!.toJson(),
      if (gigId != null) 'gigId': gigId,
      if (gigTitle != null) 'gigTitle': gigTitle,
      if (gigImage != null) 'gigImage': gigImage,
      if (sellerUserProfileId != null)
        'sellerUserProfileId': sellerUserProfileId,
      if (sellerName != null) 'sellerName': sellerName,
      if (buyerId != null) 'buyerId': buyerId,
      if (buyerName != null) 'buyerName': buyerName,
      if (conversationStatus != null)
        'conversationStatus': conversationStatus!.toJson(),
    };
  }

  /// Helper method to create a modified copy of this object.
  ConversationResponseDTO copyWith({
    int? id,
    DateTime? createdAt,
    DateTime? lastMessageAt,
    int? gigOrderId,
    GigOrderStatus? status,
    int? gigId,
    String? gigTitle,
    String? gigImage,
    int? sellerUserProfileId,
    String? sellerName,
    int? buyerId,
    String? buyerName,
    ConversationStatus? conversationStatus,
  }) {
    return ConversationResponseDTO(
      id: id ?? this.id,
      createdAt: createdAt ?? this.createdAt,
      lastMessageAt: lastMessageAt ?? this.lastMessageAt,
      gigOrderId: gigOrderId ?? this.gigOrderId,
      status: status ?? this.status,
      gigId: gigId ?? this.gigId,
      gigTitle: gigTitle ?? this.gigTitle,
      gigImage: gigImage ?? this.gigImage,
      sellerUserProfileId: sellerUserProfileId ?? this.sellerUserProfileId,
      sellerName: sellerName ?? this.sellerName,
      buyerId: buyerId ?? this.buyerId,
      buyerName: buyerName ?? this.buyerName,
      conversationStatus: conversationStatus ?? this.conversationStatus,
    );
  }
}