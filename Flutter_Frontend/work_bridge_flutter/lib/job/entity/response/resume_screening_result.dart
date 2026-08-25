class ResumeScreeningResult {
  final int? matchScore;
  final String? feedback;

  const ResumeScreeningResult({
    this.matchScore,
    this.feedback,
  });

  /// Factory constructor to create [ResumeScreeningResult] from a JSON map.
  factory ResumeScreeningResult.fromJson(Map<String, dynamic> json) {
    return ResumeScreeningResult(
      matchScore: (json['matchScore'] as num?)?.toInt(),
      feedback: json['feedback'] as String?,
    );
  }

  /// Converts this [ResumeScreeningResult] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (matchScore != null) 'matchScore': matchScore,
      if (feedback != null) 'feedback': feedback,
    };
  }

  /// Helper method to create a modified copy of this object.
  ResumeScreeningResult copyWith({
    int? matchScore,
    String? feedback,
  }) {
    return ResumeScreeningResult(
      matchScore: matchScore ?? this.matchScore,
      feedback: feedback ?? this.feedback,
    );
  }
}