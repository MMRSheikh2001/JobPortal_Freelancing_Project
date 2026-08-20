enum ResultType {
  cgpa,
  gpa,
  percentage,
  division,
  grade;


  String toJson() => name.toUpperCase();


  static ResultType? fromJson(String? value) {
    if (value == null) return null;
    return ResultType.values.firstWhere(
          (e) => e.name.toLowerCase() == value.toLowerCase(),
      orElse: () => ResultType.cgpa,
    );
  }
}