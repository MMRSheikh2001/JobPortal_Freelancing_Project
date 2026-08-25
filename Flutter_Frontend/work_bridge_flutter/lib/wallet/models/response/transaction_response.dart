import 'package:work_bridge_flutter/enums/transaction_type.dart';

class TransactionResponseDTO {
  final int? id;
  final TransactionType? type;
  final int? fromUserId;
  final String? fromUserName;
  final int? toUserId;
  final String? toUserName;
  final double? amount;
  final String? description;
  final DateTime? createdAt;

  const TransactionResponseDTO({
    this.id,
    this.type,
    this.fromUserId,
    this.fromUserName,
    this.toUserId,
    this.toUserName,
    this.amount,
    this.description,
    this.createdAt,
  });

  /// Factory constructor to create [TransactionResponseDTO] from a JSON map.
  factory TransactionResponseDTO.fromJson(Map<String, dynamic> json) {
    return TransactionResponseDTO(
      id: (json['id'] as num?)?.toInt(),
      type: json['type'] != null
          ? TransactionType.fromJson(json['type'] as String)
          : null,
      fromUserId: (json['fromUserId'] as num?)?.toInt(),
      fromUserName: json['fromUserName'] as String?,
      toUserId: (json['toUserId'] as num?)?.toInt(),
      toUserName: json['toUserName'] as String?,
      amount: (json['amount'] as num?)?.toDouble(),
      description: json['description'] as String?,
      createdAt: json['createdAt'] != null
          ? DateTime.tryParse(json['createdAt'] as String)
          : null,
    );
  }

  /// Converts this [TransactionResponseDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (id != null) 'id': id,
      if (type != null) 'type': type!.toJson(),
      if (fromUserId != null) 'fromUserId': fromUserId,
      if (fromUserName != null) 'fromUserName': fromUserName,
      if (toUserId != null) 'toUserId': toUserId,
      if (toUserName != null) 'toUserName': toUserName,
      if (amount != null) 'amount': amount,
      if (description != null) 'description': description,
      if (createdAt != null) 'createdAt': createdAt!.toIso8601String(),
    };
  }

  /// Helper method to create a modified copy of this object.
  TransactionResponseDTO copyWith({
    int? id,
    TransactionType? type,
    int? fromUserId,
    String? fromUserName,
    int? toUserId,
    String? toUserName,
    double? amount,
    String? description,
    DateTime? createdAt,
  }) {
    return TransactionResponseDTO(
      id: id ?? this.id,
      type: type ?? this.type,
      fromUserId: fromUserId ?? this.fromUserId,
      fromUserName: fromUserName ?? this.fromUserName,
      toUserId: toUserId ?? this.toUserId,
      toUserName: toUserName ?? this.toUserName,
      amount: amount ?? this.amount,
      description: description ?? this.description,
      createdAt: createdAt ?? this.createdAt,
    );
  }
}