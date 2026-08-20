import 'package:flutter/foundation.dart';
import 'package:work_bridge_flutter/auth/request/user_request.dart';



enum GigOrderStatus {
  orderPlaced,
  quoted,
  quoteAccepted,
  quoteRejected,
  delivered,
  buyerAccepted,
  buyerRejected,
  buyerCancelled,
  sellerCancelled,
  sellerDisputed,
  paymentReleased,
  refunded;

  /// Map Dart enum values back to backend JSON string representation.
  String toJson() {
    switch (this) {
      case GigOrderStatus.orderPlaced:
        return 'ORDER_PLACED';
      case GigOrderStatus.quoted:
        return 'QUOTED';
      case GigOrderStatus.quoteAccepted:
        return 'QUOTE_ACCEPTED';
      case GigOrderStatus.quoteRejected:
        return 'QUOTE_REJECTED';
      case GigOrderStatus.delivered:
        return 'DELIVERED';
      case GigOrderStatus.buyerAccepted:
        return 'BUYER_ACCEPTED';
      case GigOrderStatus.buyerRejected:
        return 'BUYER_REJECTED';
      case GigOrderStatus.buyerCancelled:
        return 'BUYER_CANCELLED';
      case GigOrderStatus.sellerCancelled:
        return 'SELLER_CANCELLED';
      case GigOrderStatus.sellerDisputed:
        return 'SELLER_DISPUTED';
      case GigOrderStatus.paymentReleased:
        return 'PAYMENT_RELEASED';
      case GigOrderStatus.refunded:
        return 'REFUNDED';
    }
  }

  /// Map JSON string value to [GigOrderStatus] enum safely.
  static GigOrderStatus? fromJson(String? value) {
    if (value == null) return null;
    switch (value.toUpperCase()) {
      case 'ORDER_PLACED':
        return GigOrderStatus.orderPlaced;
      case 'QUOTED':
        return GigOrderStatus.quoted;
      case 'QUOTE_ACCEPTED':
        return GigOrderStatus.quoteAccepted;
      case 'QUOTE_REJECTED':
        return GigOrderStatus.quoteRejected;
      case 'DELIVERED':
        return GigOrderStatus.delivered;
      case 'BUYER_ACCEPTED':
        return GigOrderStatus.buyerAccepted;
      case 'BUYER_REJECTED':
        return GigOrderStatus.buyerRejected;
      case 'BUYER_CANCELLED':
        return GigOrderStatus.buyerCancelled;
      case 'SELLER_CANCELLED':
        return GigOrderStatus.sellerCancelled;
      case 'SELLER_DISPUTED':
        return GigOrderStatus.sellerDisputed;
      case 'PAYMENT_RELEASED':
        return GigOrderStatus.paymentReleased;
      case 'REFUNDED':
        return GigOrderStatus.refunded;
      default:
        return null;
    }
  }
}

class GigOrderResponseDTO {
  final int? id;

  final double? quotedPrice;
  final double? agreedPrice;
  final double? finalPrice;

  final String? deliveryMessage;
  final String? deliveryFileUrl;

  final bool? paymentLocked;

  final GigOrderStatus? status;

  final DateTime? createdAt;
  final DateTime? quotedAt;
  final DateTime? quoteAcceptedAt;
  final DateTime? expectedDeliveryAt;
  final DateTime? deliveredAt;
  final DateTime? buyerAcceptedAt;
  final DateTime? buyerRejectedAt;
  final DateTime? buyerCancelledAt;
  final DateTime? sellerCancelledAt;
  final DateTime? sellerDisputeOpenedAt;
  final DateTime? sellerDisputeDeadline;
  final DateTime? paymentReleasedAt;
  final DateTime? refundedAt;

  final int? gigId;
  final String? gigTitle;
  final String? gigImage;

  final int? sellerId;
  final String? sellerName;

  final int? buyerId;
  final String? buyerName;

  final int? buyerUserProfileId;
  final int? buyerCompanyProfileId;

  final UserRole? buyerRole;

  final int? conversationId;

  const GigOrderResponseDTO({
    this.id,
    this.quotedPrice,
    this.agreedPrice,
    this.finalPrice,
    this.deliveryMessage,
    this.deliveryFileUrl,
    this.paymentLocked,
    this.status,
    this.createdAt,
    this.quotedAt,
    this.quoteAcceptedAt,
    this.expectedDeliveryAt,
    this.deliveredAt,
    this.buyerAcceptedAt,
    this.buyerRejectedAt,
    this.buyerCancelledAt,
    this.sellerCancelledAt,
    this.sellerDisputeOpenedAt,
    this.sellerDisputeDeadline,
    this.paymentReleasedAt,
    this.refundedAt,
    this.gigId,
    this.gigTitle,
    this.gigImage,
    this.sellerId,
    this.sellerName,
    this.buyerId,
    this.buyerName,
    this.buyerUserProfileId,
    this.buyerCompanyProfileId,
    this.buyerRole,
    this.conversationId,
  });

  /// Factory constructor to create [GigOrderResponseDTO] from a JSON map.
  factory GigOrderResponseDTO.fromJson(Map<String, dynamic> json) {
    return GigOrderResponseDTO(
      id: (json['id'] as num?)?.toInt(),
      quotedPrice: (json['quotedPrice'] as num?)?.toDouble(),
      agreedPrice: (json['agreedPrice'] as num?)?.toDouble(),
      finalPrice: (json['finalPrice'] as num?)?.toDouble(),
      deliveryMessage: json['deliveryMessage'] as String?,
      deliveryFileUrl: json['deliveryFileUrl'] as String?,
      paymentLocked: json['paymentLocked'] as bool?,
      status: json['status'] != null
          ? GigOrderStatus.fromJson(json['status'] as String)
          : null,
      createdAt: json['createdAt'] != null
          ? DateTime.tryParse(json['createdAt'] as String)
          : null,
      quotedAt: json['quotedAt'] != null
          ? DateTime.tryParse(json['quotedAt'] as String)
          : null,
      quoteAcceptedAt: json['quoteAcceptedAt'] != null
          ? DateTime.tryParse(json['quoteAcceptedAt'] as String)
          : null,
      expectedDeliveryAt: json['expectedDeliveryAt'] != null
          ? DateTime.tryParse(json['expectedDeliveryAt'] as String)
          : null,
      deliveredAt: json['deliveredAt'] != null
          ? DateTime.tryParse(json['deliveredAt'] as String)
          : null,
      buyerAcceptedAt: json['buyerAcceptedAt'] != null
          ? DateTime.tryParse(json['buyerAcceptedAt'] as String)
          : null,
      buyerRejectedAt: json['buyerRejectedAt'] != null
          ? DateTime.tryParse(json['buyerRejectedAt'] as String)
          : null,
      buyerCancelledAt: json['buyerCancelledAt'] != null
          ? DateTime.tryParse(json['buyerCancelledAt'] as String)
          : null,
      sellerCancelledAt: json['sellerCancelledAt'] != null
          ? DateTime.tryParse(json['sellerCancelledAt'] as String)
          : null,
      sellerDisputeOpenedAt: json['sellerDisputeOpenedAt'] != null
          ? DateTime.tryParse(json['sellerDisputeOpenedAt'] as String)
          : null,
      sellerDisputeDeadline: json['sellerDisputeDeadline'] != null
          ? DateTime.tryParse(json['sellerDisputeDeadline'] as String)
          : null,
      paymentReleasedAt: json['paymentReleasedAt'] != null
          ? DateTime.tryParse(json['paymentReleasedAt'] as String)
          : null,
      refundedAt: json['refundedAt'] != null
          ? DateTime.tryParse(json['refundedAt'] as String)
          : null,
      gigId: (json['gigId'] as num?)?.toInt(),
      gigTitle: json['gigTitle'] as String?,
      gigImage: json['gigImage'] as String?,
      sellerId: (json['sellerId'] as num?)?.toInt(),
      sellerName: json['sellerName'] as String?,
      buyerId: (json['buyerId'] as num?)?.toInt(),
      buyerName: json['buyerName'] as String?,
      buyerUserProfileId: (json['buyerUserProfileId'] as num?)?.toInt(),
      buyerCompanyProfileId: (json['buyerCompanyProfileId'] as num?)?.toInt(),
      buyerRole: json['buyerRole'] != null
          ? UserRole.fromJson(json['buyerRole'] as String)
          : null,
      conversationId: (json['conversationId'] as num?)?.toInt(),
    );
  }

  /// Converts this [GigOrderResponseDTO] into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (id != null) 'id': id,
      if (quotedPrice != null) 'quotedPrice': quotedPrice,
      if (agreedPrice != null) 'agreedPrice': agreedPrice,
      if (finalPrice != null) 'finalPrice': finalPrice,
      if (deliveryMessage != null) 'deliveryMessage': deliveryMessage,
      if (deliveryFileUrl != null) 'deliveryFileUrl': deliveryFileUrl,
      if (paymentLocked != null) 'paymentLocked': paymentLocked,
      if (status != null) 'status': status!.toJson(),
      if (createdAt != null) 'createdAt': createdAt!.toIso8601String(),
      if (quotedAt != null) 'quotedAt': quotedAt!.toIso8601String(),
      if (quoteAcceptedAt != null)
        'quoteAcceptedAt': quoteAcceptedAt!.toIso8601String(),
      if (expectedDeliveryAt != null)
        'expectedDeliveryAt': expectedDeliveryAt!.toIso8601String(),
      if (deliveredAt != null) 'deliveredAt': deliveredAt!.toIso8601String(),
      if (buyerAcceptedAt != null)
        'buyerAcceptedAt': buyerAcceptedAt!.toIso8601String(),
      if (buyerRejectedAt != null)
        'buyerRejectedAt': buyerRejectedAt!.toIso8601String(),
      if (buyerCancelledAt != null)
        'buyerCancelledAt': buyerCancelledAt!.toIso8601String(),
      if (sellerCancelledAt != null)
        'sellerCancelledAt': sellerCancelledAt!.toIso8601String(),
      if (sellerDisputeOpenedAt != null)
        'sellerDisputeOpenedAt': sellerDisputeOpenedAt!.toIso8601String(),
      if (sellerDisputeDeadline != null)
        'sellerDisputeDeadline': sellerDisputeDeadline!.toIso8601String(),
      if (paymentReleasedAt != null)
        'paymentReleasedAt': paymentReleasedAt!.toIso8601String(),
      if (refundedAt != null) 'refundedAt': refundedAt!.toIso8601String(),
      if (gigId != null) 'gigId': gigId,
      if (gigTitle != null) 'gigTitle': gigTitle,
      if (gigImage != null) 'gigImage': gigImage,
      if (sellerId != null) 'sellerId': sellerId,
      if (sellerName != null) 'sellerName': sellerName,
      if (buyerId != null) 'buyerId': buyerId,
      if (buyerName != null) 'buyerName': buyerName,
      if (buyerUserProfileId != null) 'buyerUserProfileId': buyerUserProfileId,
      if (buyerCompanyProfileId != null)
        'buyerCompanyProfileId': buyerCompanyProfileId,
      if (buyerRole != null) 'buyerRole': buyerRole!.toJson(),
      if (conversationId != null) 'conversationId': conversationId,
    };
  }

  /// Helper method to create a modified copy of this object.
  GigOrderResponseDTO copyWith({
    int? id,
    double? quotedPrice,
    double? agreedPrice,
    double? finalPrice,
    String? deliveryMessage,
    String? deliveryFileUrl,
    bool? paymentLocked,
    GigOrderStatus? status,
    DateTime? createdAt,
    DateTime? quotedAt,
    DateTime? quoteAcceptedAt,
    DateTime? expectedDeliveryAt,
    DateTime? deliveredAt,
    DateTime? buyerAcceptedAt,
    DateTime? buyerRejectedAt,
    DateTime? buyerCancelledAt,
    DateTime? sellerCancelledAt,
    DateTime? sellerDisputeOpenedAt,
    DateTime? sellerDisputeDeadline,
    DateTime? paymentReleasedAt,
    DateTime? refundedAt,
    int? gigId,
    String? gigTitle,
    String? gigImage,
    int? sellerId,
    String? sellerName,
    int? buyerId,
    String? buyerName,
    int? buyerUserProfileId,
    int? buyerCompanyProfileId,
    UserRole? buyerRole,
    int? conversationId,
  }) {
    return GigOrderResponseDTO(
      id: id ?? this.id,
      quotedPrice: quotedPrice ?? this.quotedPrice,
      agreedPrice: agreedPrice ?? this.agreedPrice,
      finalPrice: finalPrice ?? this.finalPrice,
      deliveryMessage: deliveryMessage ?? this.deliveryMessage,
      deliveryFileUrl: deliveryFileUrl ?? this.deliveryFileUrl,
      paymentLocked: paymentLocked ?? this.paymentLocked,
      status: status ?? this.status,
      createdAt: createdAt ?? this.createdAt,
      quotedAt: quotedAt ?? this.quotedAt,
      quoteAcceptedAt: quoteAcceptedAt ?? this.quoteAcceptedAt,
      expectedDeliveryAt: expectedDeliveryAt ?? this.expectedDeliveryAt,
      deliveredAt: deliveredAt ?? this.deliveredAt,
      buyerAcceptedAt: buyerAcceptedAt ?? this.buyerAcceptedAt,
      buyerRejectedAt: buyerRejectedAt ?? this.buyerRejectedAt,
      buyerCancelledAt: buyerCancelledAt ?? this.buyerCancelledAt,
      sellerCancelledAt: sellerCancelledAt ?? this.sellerCancelledAt,
      sellerDisputeOpenedAt:
      sellerDisputeOpenedAt ?? this.sellerDisputeOpenedAt,
      sellerDisputeDeadline:
      sellerDisputeDeadline ?? this.sellerDisputeDeadline,
      paymentReleasedAt: paymentReleasedAt ?? this.paymentReleasedAt,
      refundedAt: refundedAt ?? this.refundedAt,
      gigId: gigId ?? this.gigId,
      gigTitle: gigTitle ?? this.gigTitle,
      gigImage: gigImage ?? this.gigImage,
      sellerId: sellerId ?? this.sellerId,
      sellerName: sellerName ?? this.sellerName,
      buyerId: buyerId ?? this.buyerId,
      buyerName: buyerName ?? this.buyerName,
      buyerUserProfileId: buyerUserProfileId ?? this.buyerUserProfileId,
      buyerCompanyProfileId:
      buyerCompanyProfileId ?? this.buyerCompanyProfileId,
      buyerRole: buyerRole ?? this.buyerRole,
      conversationId: conversationId ?? this.conversationId,
    );
  }
}