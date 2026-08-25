import 'package:work_bridge_flutter/auth/request/user_request.dart';
import 'package:work_bridge_flutter/enums/withdraw_method.dart';
import 'package:work_bridge_flutter/enums/withdraw_status.dart';

class WithdrawResponseDTO {
  final int? id;
  final int? walletId;
  final double? walletBalance;
  final int? userId;
  final String? userName;
  final String? userEmail;
  final UserRole? userRole;
  final double? amount;
  final WithdrawMethod? withdrawMethod;
  final String? accountNumber;
  final String? accountName;
  final WithdrawStatus? withdrawStatus;
  final DateTime? createdAt;
  final DateTime? updatedAt;
  final String? adminRemarks;
  final String? transactionReference;

  const WithdrawResponseDTO({
    this.id,
    this.walletId,
    this.walletBalance,
    this.userId,
    this.userName,
    this.userEmail,
    this.userRole,
    this.amount,
    this.withdrawMethod,
    this.accountNumber,
    this.accountName,
    this.withdrawStatus,
    this.createdAt,
    this.updatedAt,
    this.adminRemarks,
    this.transactionReference,
  });

  /// Factory constructor to create [WithdrawResponseDTO] from a JSON map.
  factory WithdrawResponseDTO.fromJson(Map<String, dynamic> json) {
    return WithdrawResponseDTO(
      id: (json['id'] as num?)?.toInt(),
      walletId: (json['walletId'] as num?)?.toInt(),
      walletBalance: (json['walletBalance'] as num?)?.toDouble(),
      userId: (json['userId'] as num?)?.toInt(),
      userName: json['userName'] as String?,
      userEmail: json['userEmail'] as String?,
      userRole: json['userRole'] != null
          ? UserRole.fromJson(json['userRole'] as String)
          : null,
      amount: (json['amount'] as num?)?.toDouble(),
      withdrawMethod: json['withdrawMethod'] != null
          ? WithdrawMethod.fromJson(json['withdrawMethod'] as String)
          : null,
      accountNumber: json['accountNumber'] as String?,
      accountName: json['accountName'] as String?,
      withdrawStatus: json['withdrawStatus'] != null
          ? WithdrawStatus.fromJson(json['withdrawStatus'] as String)
          : null,
      createdAt: json['createdAt'] != null
          ? DateTime.tryParse(json['createdAt'] as String)
          : null,
      updatedAt: json['updatedAt'] != null
          ? DateTime.tryParse(json['updatedAt'] as String)
          : null,
      adminRemarks: json['adminRemarks'] as String?,
      transactionReference: json['transactionReference'] as String?,
    );
  }

  /// Converts this [WithdrawResponseDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (id != null) 'id': id,
      if (walletId != null) 'walletId': walletId,
      if (walletBalance != null) 'walletBalance': walletBalance,
      if (userId != null) 'userId': userId,
      if (userName != null) 'userName': userName,
      if (userEmail != null) 'userEmail': userEmail,
      if (userRole != null) 'userRole': userRole!.toJson(),
      if (amount != null) 'amount': amount,
      if (withdrawMethod != null) 'withdrawMethod': withdrawMethod!.toJson(),
      if (accountNumber != null) 'accountNumber': accountNumber,
      if (accountName != null) 'accountName': accountName,
      if (withdrawStatus != null) 'withdrawStatus': withdrawStatus!.toJson(),
      if (createdAt != null) 'createdAt': createdAt!.toIso8601String(),
      if (updatedAt != null) 'updatedAt': updatedAt!.toIso8601String(),
      if (adminRemarks != null) 'adminRemarks': adminRemarks,
      if (transactionReference != null)
        'transactionReference': transactionReference,
    };
  }

  /// Helper method to create a modified copy of this object.
  WithdrawResponseDTO copyWith({
    int? id,
    int? walletId,
    double? walletBalance,
    int? userId,
    String? userName,
    String? userEmail,
    UserRole? userRole,
    double? amount,
    WithdrawMethod? withdrawMethod,
    String? accountNumber,
    String? accountName,
    WithdrawStatus? withdrawStatus,
    DateTime? createdAt,
    DateTime? updatedAt,
    String? adminRemarks,
    String? transactionReference,
  }) {
    return WithdrawResponseDTO(
      id: id ?? this.id,
      walletId: walletId ?? this.walletId,
      walletBalance: walletBalance ?? this.walletBalance,
      userId: userId ?? this.userId,
      userName: userName ?? this.userName,
      userEmail: userEmail ?? this.userEmail,
      userRole: userRole ?? this.userRole,
      amount: amount ?? this.amount,
      withdrawMethod: withdrawMethod ?? this.withdrawMethod,
      accountNumber: accountNumber ?? this.accountNumber,
      accountName: accountName ?? this.accountName,
      withdrawStatus: withdrawStatus ?? this.withdrawStatus,
      createdAt: createdAt ?? this.createdAt,
      updatedAt: updatedAt ?? this.updatedAt,
      adminRemarks: adminRemarks ?? this.adminRemarks,
      transactionReference: transactionReference ?? this.transactionReference,
    );
  }
}