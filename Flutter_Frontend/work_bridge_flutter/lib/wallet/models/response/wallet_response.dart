class WalletResponseDTO {
  final int? id;
  final double? balance;
  final double? frozenBalance;
  final DateTime? createdAt;
  final int? userId;
  final String? userName;

  const WalletResponseDTO({
    this.id,
    this.balance,
    this.frozenBalance,
    this.createdAt,
    this.userId,
    this.userName,
  });

  /// Factory constructor to create [WalletResponseDTO] from a JSON map.
  factory WalletResponseDTO.fromJson(Map<String, dynamic> json) {
    return WalletResponseDTO(
      id: (json['id'] as num?)?.toInt(),
      balance: (json['balance'] as num?)?.toDouble(),
      frozenBalance: (json['frozenBalance'] as num?)?.toDouble(),
      createdAt: json['createdAt'] != null
          ? DateTime.tryParse(json['createdAt'] as String)
          : null,
      userId: (json['userId'] as num?)?.toInt(),
      userName: json['userName'] as String?,
    );
  }

  /// Converts this [WalletResponseDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (id != null) 'id': id,
      if (balance != null) 'balance': balance,
      if (frozenBalance != null) 'frozenBalance': frozenBalance,
      if (createdAt != null) 'createdAt': createdAt!.toIso8601String(),
      if (userId != null) 'userId': userId,
      if (userName != null) 'userName': userName,
    };
  }

  /// Helper method to create a modified copy of this object.
  WalletResponseDTO copyWith({
    int? id,
    double? balance,
    double? frozenBalance,
    DateTime? createdAt,
    int? userId,
    String? userName,
  }) {
    return WalletResponseDTO(
      id: id ?? this.id,
      balance: balance ?? this.balance,
      frozenBalance: frozenBalance ?? this.frozenBalance,
      createdAt: createdAt ?? this.createdAt,
      userId: userId ?? this.userId,
      userName: userName ?? this.userName,
    );
  }
}