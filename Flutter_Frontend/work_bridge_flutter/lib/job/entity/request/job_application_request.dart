class JobApplicationRequestDTO {
  final int? jobId;
  final int? userProfileId;

  const JobApplicationRequestDTO({
    this.jobId,
    this.userProfileId,
  });

  /// Factory constructor to create [JobApplicationRequestDTO] from a JSON map.
  factory JobApplicationRequestDTO.fromJson(Map<String, dynamic> json) {
    return JobApplicationRequestDTO(
      jobId: (json['jobId'] as num?)?.toInt(),
      userProfileId: (json['userProfileId'] as num?)?.toInt(),
    );
  }

  /// Converts this [JobApplicationRequestDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (jobId != null) 'jobId': jobId,
      if (userProfileId != null) 'userProfileId': userProfileId,
    };
  }

  /// Helper method to create a modified copy of this object.
  JobApplicationRequestDTO copyWith({
    int? jobId,
    int? userProfileId,
  }) {
    return JobApplicationRequestDTO(
      jobId: jobId ?? this.jobId,
      userProfileId: userProfileId ?? this.userProfileId,
    );
  }
}