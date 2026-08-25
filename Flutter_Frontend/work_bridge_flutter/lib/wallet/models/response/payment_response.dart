import 'package:work_bridge_flutter/auth/request/user_request.dart';
import 'package:work_bridge_flutter/enums/payment_status.dart';

class PaymentResponseDTO {
  final int? id;
  final PaymentStatus? paymentStatus;
  final DateTime? createdAt;
  final DateTime? updatedAt;
  final double? amount;
  final int? userId;
  final String? userName;
  final UserRole? userRole;
  final String? gatewayTransactionId;
  final String? validationId;
  final String? paymentMethod;
  final String? gateway;
  final String? failureReason;

  const PaymentResponseDTO({
    this.id,
    this.paymentStatus,
    this.createdAt,
    this.updatedAt,
    this.amount,
    this.userId,
    this.userName,
    this.userRole,
    this.gatewayTransactionId,
    this.validationId,
    this.paymentMethod,
    this.gateway,
    this.failureReason,
  });

  /// Factory constructor to create [PaymentResponseDTO] from a JSON map.
  factory PaymentResponseDTO.fromJson(Map<String, dynamic> json) {
    return PaymentResponseDTO(
      id: (json['id'] as num?)?.toInt(),
      paymentStatus: json['paymentStatus'] != null
          ? PaymentStatus.fromJson(json['paymentStatus'] as String)
          : null,
      createdAt: json['createdAt'] != null
          ? DateTime.tryParse(json['createdAt'] as String)
          : null,
      updatedAt: json['updatedAt'] != null
          ? DateTime.tryParse(json['updatedAt'] as String)
          : null,
      amount: (json['amount'] as num?)?.toDouble(),
      userId: (json['userId'] as num?)?.toInt(),
      userName: json['userName'] as String?,
      userRole: json['userRole'] != null
          ? UserRole.fromJson(json['userRole'] as String)
          : null,
      gatewayTransactionId: json['gatewayTransactionId'] as String?,
      validationId: json['validationId'] as String?,
      paymentMethod: json['paymentMethod'] as String?,
      gateway: json['gateway'] as String?,
      failureReason: json['failureReason'] as String?,
    );
  }

  /// Converts this [PaymentResponseDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (id != null) 'id': id,
      if (paymentStatus != null) 'paymentStatus': paymentStatus!.toJson(),
      if (createdAt != null) 'createdAt': createdAt!.toIso8601String(),
      if (updatedAt != null) 'updatedAt': updatedAt!.toIso8601String(),
      if (amount != null) 'amount': amount,
      if (userId != null) 'userId': userId,
      if (userName != null) 'userName': userName,
      if (userRole != null) 'userRole': userRole!.toJson(),
      if (gatewayTransactionId != null)
        'gatewayTransactionId': gatewayTransactionId,
      if (validationId != null) 'validationId': validationId,
      if (paymentMethod != null) 'paymentMethod': paymentMethod,
      if (gateway != null) 'gateway': gateway,
      if (failureReason != null) 'failureReason': failureReason,
    };
  }

  /// Helper method to create a modified copy of this object.
  PaymentResponseDTO copyWith({
    int? id,
    PaymentStatus? paymentStatus,
    DateTime? createdAt,
    DateTime? updatedAt,
    double? amount,
    int? userId,
    String? userName,
    UserRole? userRole,
    String? gatewayTransactionId,
    String? validationId,
    String? paymentMethod,
    String? gateway,
    String? failureReason,
  }) {
    return PaymentResponseDTO(
      id: id ?? this.id,
      paymentStatus: paymentStatus ?? this.paymentStatus,
      createdAt: createdAt ?? this.createdAt,
      updatedAt: updatedAt ?? this.updatedAt,
      amount: amount ?? this.amount,
      userId: userId ?? this.userId,
      userName: userName ?? this.userName,
      userRole: userRole ?? this.userRole,
      gatewayTransactionId: gatewayTransactionId ?? this.gatewayTransactionId,
      validationId: validationId ?? this.validationId,
      paymentMethod: paymentMethod ?? this.paymentMethod,
      gateway: gateway ?? this.gateway,
      failureReason: failureReason ?? this.failureReason,
    );
  }
}
