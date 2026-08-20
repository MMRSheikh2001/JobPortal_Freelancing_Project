enum EducationLevel {
  ssc,
  hsc,
  diploma,
  bachelor,
  pgd,
  masters,
  mphil,
  phd;


  String toJson() => name.toUpperCase();

  
  static EducationLevel? fromJson(String? value) {
    if (value == null) return null;
    return EducationLevel.values.firstWhere(
          (e) => e.name.toLowerCase() == value.toLowerCase(),
      orElse: () => EducationLevel.ssc,
    );
  }
}