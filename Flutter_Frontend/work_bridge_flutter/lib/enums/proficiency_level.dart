enum ProficiencyLevel {
  beginner('BEGINNER'),
  intermediate('INTERMEDIATE'),
  advanced('ADVANCED'),
  expert('EXPERT');

  final String value;

  const ProficiencyLevel(this.value);

  /// Factory constructor to create [ProficiencyLevel] from a JSON string.
  factory ProficiencyLevel.fromJson(String json) {
    return ProficiencyLevel.values.firstWhere(
          (e) => e.value.toLowerCase() == json.toLowerCase(),
      orElse: () => ProficiencyLevel.beginner,
    );
  }

  /// Converts this [ProficiencyLevel] enum value to its string representation.
  String toJson() => value;
}