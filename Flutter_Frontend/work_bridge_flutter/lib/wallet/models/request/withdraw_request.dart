import 'package:work_bridge_flutter/enums/withdraw_method.dart';

class WithdrawRequestDTO {
  final int? userId;
  final double? amount;
  final WithdrawMethod? withdrawMethod;
  final String? accountNumber;
  final String? accountName;

  const WithdrawRequestDTO({
    this.userId,
    this.amount,
    this.withdrawMethod,
    this.accountNumber,
    this.accountName,
  });

  /// Factory constructor to create [WithdrawRequestDTO] from a JSON map.
  factory WithdrawRequestDTO.fromJson(Map<String, dynamic> json) {
    return WithdrawRequestDTO(
      userId: (json['userId'] as num?)?.toInt(),
      amount: (json['amount'] as num?)?.toDouble(),
      withdrawMethod: json['withdrawMethod'] != null
          ? WithdrawMethod.fromJson(json['withdrawMethod'] as String)
          : null,
      accountNumber: json['accountNumber'] as String?,
      accountName: json['accountName'] as String?,
    );
  }

  /// Converts this [WithdrawRequestDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (userId != null) 'userId': userId,
      if (amount != null) 'amount': amount,
      if (withdrawMethod != null) 'withdrawMethod': withdrawMethod!.toJson(),
      if (accountNumber != null) 'accountNumber': accountNumber,
      if (accountName != null) 'accountName': accountName,
    };
  }

  /// Helper method to create a modified copy of this object.
  WithdrawRequestDTO copyWith({
    int? userId,
    double? amount,
    WithdrawMethod? withdrawMethod,
    String? accountNumber,
    String? accountName,
  }) {
    return WithdrawRequestDTO(
      userId: userId ?? this.userId,
      amount: amount ?? this.amount,
      withdrawMethod: withdrawMethod ?? this.withdrawMethod,
      accountNumber: accountNumber ?? this.accountNumber,
      accountName: accountName ?? this.accountName,
    );
  }
}