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

class JobApplicationResponseDTO {
  final int? id;
  final ApplicationStatus? status;
  final DateTime? appliedAt;
  final DateTime? aiDeadlineDate;
  final String? companyNotes;
  final int? jobId;
  final String? jobTitle;
  final String? jobDescription;
  final int? companyProfileId;
  final String? companyName;
  final int? companyUserId;
  final String? companyUserEmail;
  final String? companyLogo;
  final int? userProfileId;
  final String? userName;
  final String? userImage;
  final int? userId;
  final String? userEmail;

  // AI Completed
  final int? aiMatchScore;
  final String? aiMatchFeedback;
  final int? aiInterviewScore;
  final int? aiFinalScore;
  final bool? aiInterviewCompleted;
  final DateTime? aiCompletedAt;
  final bool? aiShortlisted;

  // AI Integration
  final bool? aiScreeningEnabled;
  final bool? aiCvScreeningEnabled;
  final bool? aiInterviewEnabled;
  final int? aiMatchThreshold;
  final int? aiQuestionCount;

  const JobApplicationResponseDTO({
    this.id,
    this.status,
    this.appliedAt,
    this.aiDeadlineDate,
    this.companyNotes,
    this.jobId,
    this.jobTitle,
    this.jobDescription,
    this.companyProfileId,
    this.companyName,
    this.companyUserId,
    this.companyUserEmail,
    this.companyLogo,
    this.userProfileId,
    this.userName,
    this.userImage,
    this.userId,
    this.userEmail,
    this.aiMatchScore,
    this.aiMatchFeedback,
    this.aiInterviewScore,
    this.aiFinalScore,
    this.aiInterviewCompleted,
    this.aiCompletedAt,
    this.aiShortlisted,
    this.aiScreeningEnabled,
    this.aiCvScreeningEnabled,
    this.aiInterviewEnabled,
    this.aiMatchThreshold,
    this.aiQuestionCount,
  });

  /// Factory constructor to create a [JobApplicationResponseDTO] from a JSON map.
  factory JobApplicationResponseDTO.fromJson(Map<String, dynamic> json) {
    return JobApplicationResponseDTO(
      id: (json['id'] as num?)?.toInt(),
      status: json['status'] != null
          ? ApplicationStatus.fromJson(json['status'] as String)
          : null,
      appliedAt: json['appliedAt'] != null
          ? DateTime.tryParse(json['appliedAt'] as String)
          : null,
      aiDeadlineDate: json['aiDeadlineDate'] != null
          ? DateTime.tryParse(json['aiDeadlineDate'] as String)
          : null,
      companyNotes: json['companyNotes'] as String?,
      jobId: (json['jobId'] as num?)?.toInt(),
      jobTitle: json['jobTitle'] as String?,
      jobDescription: json['jobDescription'] as String?,
      companyProfileId: (json['companyProfileId'] as num?)?.toInt(),
      companyName: json['companyName'] as String?,
      companyUserId: (json['companyUserId'] as num?)?.toInt(),
      companyUserEmail: json['companyUserEmail'] as String?,
      companyLogo: json['companyLogo'] as String?,
      userProfileId: (json['userProfileId'] as num?)?.toInt(),
      userName: json['userName'] as String?,
      userImage: json['userImage'] as String?,
      userId: (json['userId'] as num?)?.toInt(),
      userEmail: json['userEmail'] as String?,
      aiMatchScore: (json['aiMatchScore'] as num?)?.toInt(),
      aiMatchFeedback: json['aiMatchFeedback'] as String?,
      aiInterviewScore: (json['aiInterviewScore'] as num?)?.toInt(),
      aiFinalScore: (json['aiFinalScore'] as num?)?.toInt(),
      aiInterviewCompleted: json['aiInterviewCompleted'] as bool?,
      aiCompletedAt: json['aiCompletedAt'] != null
          ? DateTime.tryParse(json['aiCompletedAt'] as String)
          : null,
      aiShortlisted: json['aiShortlisted'] as bool?,
      aiScreeningEnabled: json['aiScreeningEnabled'] as bool?,
      aiCvScreeningEnabled: json['aiCvScreeningEnabled'] as bool?,
      aiInterviewEnabled: json['aiInterviewEnabled'] as bool?,
      aiMatchThreshold: (json['aiMatchThreshold'] as num?)?.toInt(),
      aiQuestionCount: (json['aiQuestionCount'] as num?)?.toInt(),
    );
  }

  /// Converts this [JobApplicationResponseDTO] into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (id != null) 'id': id,
      if (status != null) 'status': status!.toJson(),
      if (appliedAt != null) 'appliedAt': appliedAt!.toIso8601String(),
      if (aiDeadlineDate != null)
        'aiDeadlineDate': aiDeadlineDate!.toIso8601String(),
      if (companyNotes != null) 'companyNotes': companyNotes,
      if (jobId != null) 'jobId': jobId,
      if (jobTitle != null) 'jobTitle': jobTitle,
      if (jobDescription != null) 'jobDescription': jobDescription,
      if (companyProfileId != null) 'companyProfileId': companyProfileId,
      if (companyName != null) 'companyName': companyName,
      if (companyUserId != null) 'companyUserId': companyUserId,
      if (companyUserEmail != null) 'companyUserEmail': companyUserEmail,
      if (companyLogo != null) 'companyLogo': companyLogo,
      if (userProfileId != null) 'userProfileId': userProfileId,
      if (userName != null) 'userName': userName,
      if (userImage != null) 'userImage': userImage,
      if (userId != null) 'userId': userId,
      if (userEmail != null) 'userEmail': userEmail,
      if (aiMatchScore != null) 'aiMatchScore': aiMatchScore,
      if (aiMatchFeedback != null) 'aiMatchFeedback': aiMatchFeedback,
      if (aiInterviewScore != null) 'aiInterviewScore': aiInterviewScore,
      if (aiFinalScore != null) 'aiFinalScore': aiFinalScore,
      if (aiInterviewCompleted != null)
        'aiInterviewCompleted': aiInterviewCompleted,
      if (aiCompletedAt != null)
        'aiCompletedAt': aiCompletedAt!.toIso8601String(),
      if (aiShortlisted != null) 'aiShortlisted': aiShortlisted,
      if (aiScreeningEnabled != null) 'aiScreeningEnabled': aiScreeningEnabled,
      if (aiCvScreeningEnabled != null)
        'aiCvScreeningEnabled': aiCvScreeningEnabled,
      if (aiInterviewEnabled != null) 'aiInterviewEnabled': aiInterviewEnabled,
      if (aiMatchThreshold != null) 'aiMatchThreshold': aiMatchThreshold,
      if (aiQuestionCount != null) 'aiQuestionCount': aiQuestionCount,
    };
  }

  /// Helper method to create a modified copy of this object.
  JobApplicationResponseDTO copyWith({
    int? id,
    ApplicationStatus? status,
    DateTime? appliedAt,
    DateTime? aiDeadlineDate,
    String? companyNotes,
    int? jobId,
    String? jobTitle,
    String? jobDescription,
    int? companyProfileId,
    String? companyName,
    int? companyUserId,
    String? companyUserEmail,
    String? companyLogo,
    int? userProfileId,
    String? userName,
    String? userImage,
    int? userId,
    String? userEmail,
    int? aiMatchScore,
    String? aiMatchFeedback,
    int? aiInterviewScore,
    int? aiFinalScore,
    bool? aiInterviewCompleted,
    DateTime? aiCompletedAt,
    bool? aiShortlisted,
    bool? aiScreeningEnabled,
    bool? aiCvScreeningEnabled,
    bool? aiInterviewEnabled,
    int? aiMatchThreshold,
    int? aiQuestionCount,
  }) {
    return JobApplicationResponseDTO(
      id: id ?? this.id,
      status: status ?? this.status,
      appliedAt: appliedAt ?? this.appliedAt,
      aiDeadlineDate: aiDeadlineDate ?? this.aiDeadlineDate,
      companyNotes: companyNotes ?? this.companyNotes,
      jobId: jobId ?? this.jobId,
      jobTitle: jobTitle ?? this.jobTitle,
      jobDescription: jobDescription ?? this.jobDescription,
      companyProfileId: companyProfileId ?? this.companyProfileId,
      companyName: companyName ?? this.companyName,
      companyUserId: companyUserId ?? this.companyUserId,
      companyUserEmail: companyUserEmail ?? this.companyUserEmail,
      companyLogo: companyLogo ?? this.companyLogo,
      userProfileId: userProfileId ?? this.userProfileId,
      userName: userName ?? this.userName,
      userImage: userImage ?? this.userImage,
      userId: userId ?? this.userId,
      userEmail: userEmail ?? this.userEmail,
      aiMatchScore: aiMatchScore ?? this.aiMatchScore,
      aiMatchFeedback: aiMatchFeedback ?? this.aiMatchFeedback,
      aiInterviewScore: aiInterviewScore ?? this.aiInterviewScore,
      aiFinalScore: aiFinalScore ?? this.aiFinalScore,
      aiInterviewCompleted: aiInterviewCompleted ?? this.aiInterviewCompleted,
      aiCompletedAt: aiCompletedAt ?? this.aiCompletedAt,
      aiShortlisted: aiShortlisted ?? this.aiShortlisted,
      aiScreeningEnabled: aiScreeningEnabled ?? this.aiScreeningEnabled,
      aiCvScreeningEnabled: aiCvScreeningEnabled ?? this.aiCvScreeningEnabled,
      aiInterviewEnabled: aiInterviewEnabled ?? this.aiInterviewEnabled,
      aiMatchThreshold: aiMatchThreshold ?? this.aiMatchThreshold,
      aiQuestionCount: aiQuestionCount ?? this.aiQuestionCount,
    );
  }
}
