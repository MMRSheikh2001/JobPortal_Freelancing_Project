enum JobType {
  fullTime,
  partTime,
  contract,
  internship,
  freelance,
  remote,
  temporary,
  volunteer;

  String toJson() {
    switch (this) {
      case JobType.fullTime:
        return 'FULL_TIME';
      case JobType.partTime:
        return 'PART_TIME';
      case JobType.contract:
        return 'CONTRACT';
      case JobType.internship:
        return 'INTERNSHIP';
      case JobType.freelance:
        return 'FREELANCE';
      case JobType.remote:
        return 'REMOTE';
      case JobType.temporary:
        return 'TEMPORARY';
      case JobType.volunteer:
        return 'VOLUNTEER';
    }
  }

  /// Deserializes a string value from JSON into a [JobType] enum safely.
  static JobType? fromJson(String? value) {
    if (value == null) return null;
    switch (value.toUpperCase()) {
      case 'FULL_TIME':
        return JobType.fullTime;
      case 'PART_TIME':
        return JobType.partTime;
      case 'CONTRACT':
        return JobType.contract;
      case 'INTERNSHIP':
        return JobType.internship;
      case 'FREELANCE':
        return JobType.freelance;
      case 'REMOTE':
        return JobType.remote;
      case 'TEMPORARY':
        return JobType.temporary;
      case 'VOLUNTEER':
        return JobType.volunteer;
      default:
        return null;
    }
  }
}