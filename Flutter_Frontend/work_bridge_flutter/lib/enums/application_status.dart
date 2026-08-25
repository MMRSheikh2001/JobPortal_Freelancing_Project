enum ApplicationStatus {
  applied,
  aiPending,
  aiCompleted,
  automaticQualified,
  companyShortlisted,
  hired,
  rejected,
  withdrawn;

  /// Map Dart enum values back to backend JSON string representation.
  String toJson() {
    switch (this) {
      case ApplicationStatus.applied:
        return 'APPLIED';
      case ApplicationStatus.aiPending:
        return 'AI_PENDING';
      case ApplicationStatus.aiCompleted:
        return 'AI_COMPLETED';
      case ApplicationStatus.automaticQualified:
        return 'AUTOMATIC_QUALIFIED';
      case ApplicationStatus.companyShortlisted:
        return 'COMPANY_SHORTLISTED';
      case ApplicationStatus.hired:
        return 'HIRED';
      case ApplicationStatus.rejected:
        return 'REJECTED';
      case ApplicationStatus.withdrawn:
        return 'WITHDRAWN';
    }
  }

  /// Map JSON string value to [ApplicationStatus] enum safely.
  static ApplicationStatus? fromJson(String? value) {
    if (value == null) return null;
    switch (value.toUpperCase()) {
      case 'APPLIED':
        return ApplicationStatus.applied;
      case 'AI_PENDING':
        return ApplicationStatus.aiPending;
      case 'AI_COMPLETED':
        return ApplicationStatus.aiCompleted;
      case 'AUTOMATIC_QUALIFIED':
        return ApplicationStatus.automaticQualified;
      case 'COMPANY_SHORTLISTED':
        return ApplicationStatus.companyShortlisted;
      case 'HIRED':
        return ApplicationStatus.hired;
      case 'REJECTED':
        return ApplicationStatus.rejected;
      case 'WITHDRAWN':
        return ApplicationStatus.withdrawn;
      default:
        return null;
    }
  }
}