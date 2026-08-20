enum TrainingType {
  online('Online'),
  offline('Offline'),
  bootcamp('Bootcamp'),
  workshop('Workshop'),
  certification('Certification');

  final String value;

  const TrainingType(this.value);

  /// Factory constructor to create [TrainingType] from a JSON string.
  factory TrainingType.fromJson(String json) {
    return TrainingType.values.firstWhere(
          (e) => e.value.toLowerCase() == json.toLowerCase(),
      orElse: () => TrainingType.online,
    );
  }

  /// Converts this [TrainingType] enum value to its string representation.
  String toJson() => value;
}