enum NotificationType {
  // Jobs
  jobApplied('JOB_APPLIED'),
  jobShortlisted('JOB_SHORTLISTED'),
  jobRejected('JOB_REJECTED'),
  jobHired('JOB_HIRED'),

  // Gig
  gigApplication('GIG_APPLICATION'),
  gigOrder('GIG_ORDER'),
  gigCompleted('GIG_COMPLETED'),

  // Wallet
  depositSuccess('DEPOSIT_SUCCESS'),
  withdrawApproved('WITHDRAW_APPROVED'),
  withdrawRejected('WITHDRAW_REJECTED'),

  // General
  system('SYSTEM'),
  adminMessage('ADMIN_MESSAGE');

  final String value;

  const NotificationType(this.value);

  /// Factory constructor to create [NotificationType] from a JSON string.
  factory NotificationType.fromJson(String json) {
    return NotificationType.values.firstWhere(
          (e) => e.value.toLowerCase() == json.toLowerCase(),
      orElse: () => NotificationType.system,
    );
  }

  /// Converts this [NotificationType] enum value to its string representation.
  String toJson() => value;
}