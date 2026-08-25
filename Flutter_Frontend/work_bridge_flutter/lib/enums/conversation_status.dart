enum ConversationStatus {
  active('ACTIVE'),
  closed('CLOSED');

  final String value;

  const ConversationStatus(this.value);

  /// Factory constructor to create [ConversationStatus] from a JSON string.
  factory ConversationStatus.fromJson(String json) {
    return ConversationStatus.values.firstWhere(
          (e) => e.value.toLowerCase() == json.toLowerCase(),
      orElse: () => ConversationStatus.active,
    );
  }

  /// Converts this [ConversationStatus] enum value to its string representation.
  String toJson() => value;
}