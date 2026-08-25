enum EmploymentType {
  fullTime,
  partTime,
  contract,
  internship,
  freelance;

  String toJson() {
    switch (this) {
      case EmploymentType.fullTime:
        return 'Full_Time';
      case EmploymentType.partTime:
        return 'Part_Time';
      case EmploymentType.contract:
        return 'Contract';
      case EmploymentType.internship:
        return 'Internship';
      case EmploymentType.freelance:
        return 'Freelance';
    }
  }

  static EmploymentType? fromJson(String? value) {
    if (value == null) return null;
    switch (value.toUpperCase()) {
      case 'FULL_TIME':
        return EmploymentType.fullTime;
      case 'PART_TIME':
        return EmploymentType.partTime;
      case 'CONTRACT':
        return EmploymentType.contract;
      case 'INTERNSHIP':
        return EmploymentType.internship;
      case 'FREELANCE':
        return EmploymentType.freelance;
      default:
        return null;
    }
  }
}