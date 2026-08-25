enum WithdrawMethod {
  bkash('BKASH'),
  nagad('NAGAD'),
  bank('BANK');

  final String value;

  const WithdrawMethod(this.value);

  /// Factory constructor to create [WithdrawMethod] from a JSON string.
  factory WithdrawMethod.fromJson(String json) {
    return WithdrawMethod.values.firstWhere(
          (e) => e.value.toLowerCase() == json.toLowerCase(),
      orElse: () => WithdrawMethod.bkash,
    );
  }

  /// Converts this [WithdrawMethod] enum value to its string representation.
  String toJson() => value;
}