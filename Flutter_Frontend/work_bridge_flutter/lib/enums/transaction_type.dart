enum TransactionType {
  deposit('DEPOSIT'),
  withdraw('WITHDRAW'),
  jobPostPayment('JOB_POST_PAYMENT'),
  sellerPayout('SELLER_PAYOUT'),
  platformCommission('PLATFORM_COMMISSION'),
  refund('REFUND'),
  freeze('FREEZE');

  final String value;

  const TransactionType(this.value);

  /// Factory constructor to create [TransactionType] from a JSON string.
  factory TransactionType.fromJson(String json) {
    return TransactionType.values.firstWhere(
          (e) => e.value.toLowerCase() == json.toLowerCase(),
      orElse: () => TransactionType.deposit,
    );
  }

  /// Converts this [TransactionType] enum value to its string representation.
  String toJson() => value;
}