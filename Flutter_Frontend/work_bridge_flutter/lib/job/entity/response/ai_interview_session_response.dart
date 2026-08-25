import 'package:work_bridge_flutter/job/entity/response/interview_question.dart';

class AIInterviewSessionResponseDTO {
  final int? applicationId;
  final DateTime? startedAt;
  final DateTime? completedAt;
  final int? totalScore;
  final bool? completed;
  final List<InterviewQuestion>? questions;

  const AIInterviewSessionResponseDTO({
    this.applicationId,
    this.startedAt,
    this.completedAt,
    this.totalScore,
    this.completed,
    this.questions,
  });

  /// Factory constructor to create [AIInterviewSessionResponseDTO] from a JSON map.
  factory AIInterviewSessionResponseDTO.fromJson(Map<String, dynamic> json) {
    return AIInterviewSessionResponseDTO(
      applicationId: (json['applicationId'] as num?)?.toInt(),
      startedAt: json['startedAt'] != null
          ? DateTime.tryParse(json['startedAt'] as String)
          : null,
      completedAt: json['completedAt'] != null
          ? DateTime.tryParse(json['completedAt'] as String)
          : null,
      totalScore: (json['totalScore'] as num?)?.toInt(),
      completed: json['completed'] as bool?,
      questions: (json['questions'] as List<dynamic>?)
          ?.map((e) => InterviewQuestion.fromJson(e as Map<String, dynamic>))
          .toList(),
    );
  }

  /// Converts this [AIInterviewSessionResponseDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (applicationId != null) 'applicationId': applicationId,
      if (startedAt != null) 'startedAt': startedAt!.toIso8601String(),
      if (completedAt != null) 'completedAt': completedAt!.toIso8601String(),
      if (totalScore != null) 'totalScore': totalScore,
      if (completed != null) 'completed': completed,
      if (questions != null)
        'questions': questions!.map((e) => e.toJson()).toList(),
    };
  }

  /// Helper method to create a modified copy of this object.
  AIInterviewSessionResponseDTO copyWith({
    int? applicationId,
    DateTime? startedAt,
    DateTime? completedAt,
    int? totalScore,
    bool? completed,
    List<InterviewQuestion>? questions,
  }) {
    return AIInterviewSessionResponseDTO(
      applicationId: applicationId ?? this.applicationId,
      startedAt: startedAt ?? this.startedAt,
      completedAt: completedAt ?? this.completedAt,
      totalScore: totalScore ?? this.totalScore,
      completed: completed ?? this.completed,
      questions: questions ?? this.questions,
    );
  }
}