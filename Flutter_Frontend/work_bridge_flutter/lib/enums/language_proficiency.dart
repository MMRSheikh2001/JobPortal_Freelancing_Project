enum LanguageProficiency {
  beginner('BEGINNER'),
  intermediate('INTERMEDIATE'),
  advanced('ADVANCED'),
  native('NATIVE');

  final String value;

  const LanguageProficiency(this.value);

  /// Factory constructor to create [LanguageProficiency] from a JSON string.
  factory LanguageProficiency.fromJson(String json) {
    return LanguageProficiency.values.firstWhere(
          (e) => e.value.toLowerCase() == json.toLowerCase(),
      orElse: () => LanguageProficiency.beginner,
    );
  }

  /// Converts this [LanguageProficiency] enum value to its string representation.
  String toJson() => value;
}