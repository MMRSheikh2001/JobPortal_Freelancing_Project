import 'package:work_bridge_flutter/enums/payment_status.dart';

class DepositSessionResponseDTO {
  final int? paymentId;
  final String? gatewayTransactionId;
  final String? gatewayPageUrl;
  final PaymentStatus? paymentStatus;

  const DepositSessionResponseDTO({
    this.paymentId,
    this.gatewayTransactionId,
    this.gatewayPageUrl,
    this.paymentStatus,
  });

  /// Factory constructor to create [DepositSessionResponseDTO] from a JSON map.
  factory DepositSessionResponseDTO.fromJson(Map<String, dynamic> json) {
    return DepositSessionResponseDTO(
      paymentId: (json['paymentId'] as num?)?.toInt(),
      gatewayTransactionId: json['gatewayTransactionId'] as String?,
      gatewayPageUrl: json['gatewayPageUrl'] as String?,
      paymentStatus: json['paymentStatus'] != null
          ? PaymentStatus.fromJson(json['paymentStatus'] as String)
          : null,
    );
  }

  /// Converts this [DepositSessionResponseDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (paymentId != null) 'paymentId': paymentId,
      if (gatewayTransactionId != null)
        'gatewayTransactionId': gatewayTransactionId,
      if (gatewayPageUrl != null) 'gatewayPageUrl': gatewayPageUrl,
      if (paymentStatus != null) 'paymentStatus': paymentStatus!.toJson(),
    };
  }

  /// Helper method to create a modified copy of this object.
  DepositSessionResponseDTO copyWith({
    int? paymentId,
    String? gatewayTransactionId,
    String? gatewayPageUrl,
    PaymentStatus? paymentStatus,
  }) {
    return DepositSessionResponseDTO(
      paymentId: paymentId ?? this.paymentId,
      gatewayTransactionId: gatewayTransactionId ?? this.gatewayTransactionId,
      gatewayPageUrl: gatewayPageUrl ?? this.gatewayPageUrl,
      paymentStatus: paymentStatus ?? this.paymentStatus,
    );
  }
}