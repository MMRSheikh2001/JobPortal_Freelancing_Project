enum WorkPlaceType {
  onsite,
  remote,
  hybrid;

  String toJson() => name.toUpperCase();

  static WorkPlaceType? fromJson(String? value) {
    if (value == null) return null;
    return WorkPlaceType.values.firstWhere(
          (e) => e.name.toLowerCase() == value.toLowerCase(),
      orElse: () => WorkPlaceType.onsite,
    );
  }
}