enum WithdrawStatus {
  pending('PENDING'),
  approved('APPROVED'),
  rejected('REJECTED');

  final String value;

  const WithdrawStatus(this.value);

  /// Factory constructor to create [WithdrawStatus] from a JSON string.
  factory WithdrawStatus.fromJson(String json) {
    return WithdrawStatus.values.firstWhere(
          (e) => e.value.toLowerCase() == json.toLowerCase(),
      orElse: () => WithdrawStatus.pending,
    );
  }

  /// Converts this [WithdrawStatus] enum value to its string representation.
  String toJson() => value;
}