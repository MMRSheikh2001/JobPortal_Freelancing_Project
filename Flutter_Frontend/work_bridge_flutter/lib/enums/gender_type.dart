enum GenderType {
  male,
  female,
  other;


  String toJson() => name.toUpperCase();

  
  static GenderType? fromJson(String? value) {
    if (value == null) return null;
    return GenderType.values.firstWhere(
          (e) => e.name.toLowerCase() == value.toLowerCase(),
      orElse: () => GenderType.other,
    );
  }
}