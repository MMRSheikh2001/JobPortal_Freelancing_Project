enum PaymentStatus {
  pending('PENDING'),
  success('SUCCESS'),
  failed('FAILED'),
  cancelled('CANCELLED');

  final String value;

  const PaymentStatus(this.value);

  /// Factory constructor to create [PaymentStatus] from a JSON string.
  factory PaymentStatus.fromJson(String json) {
    return PaymentStatus.values.firstWhere(
          (e) => e.value.toLowerCase() == json.toLowerCase(),
      orElse: () => PaymentStatus.pending,
    );
  }

  /// Converts this [PaymentStatus] enum value to its string representation.
  String toJson() => value;
}