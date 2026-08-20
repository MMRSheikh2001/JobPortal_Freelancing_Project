import 'package:work_bridge_flutter/enums/education_level.dart';
import 'package:work_bridge_flutter/enums/result_type.dart';

class EducationRequestDTO {
  final EducationLevel? educationLevel;
  final String? board;
  final String? institution;
  final String? fieldOfStudy;
  final ResultType? resultType;
  final double? result;
  final double? outOf;
  final String? gradeOrDivision;
  final DateTime? startDate;
  final DateTime? endDate;
  final int? userProfileId;

  const EducationRequestDTO({
    this.educationLevel,
    this.board,
    this.institution,
    this.fieldOfStudy,
    this.resultType,
    this.result,
    this.outOf,
    this.gradeOrDivision,
    this.startDate,
    this.endDate,
    this.userProfileId,
  });

  /// Factory constructor to create [EducationRequestDTO] from a JSON map.
  factory EducationRequestDTO.fromJson(Map<String, dynamic> json) {
    return EducationRequestDTO(
      educationLevel: json['educationLevel'] != null
          ? EducationLevel.fromJson(json['educationLevel'] as String)
          : null,
      board: json['board'] as String?,
      institution: json['institution'] as String?,
      fieldOfStudy: json['fieldOfStudy'] as String?,
      resultType: json['resultType'] != null
          ? ResultType.fromJson(json['resultType'] as String)
          : null,
      result: (json['result'] as num?)?.toDouble(),
      outOf: (json['outOf'] as num?)?.toDouble(),
      gradeOrDivision: json['gradeOrDivision'] as String?,
      startDate: json['startDate'] != null
          ? DateTime.tryParse(json['startDate'] as String)
          : null,
      endDate: json['endDate'] != null
          ? DateTime.tryParse(json['endDate'] as String)
          : null,
      userProfileId: (json['userProfileId'] as num?)?.toInt(),
    );
  }

  /// Converts this [EducationRequestDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (educationLevel != null) 'educationLevel': educationLevel!.toJson(),
      if (board != null) 'board': board,
      if (institution != null) 'institution': institution,
      if (fieldOfStudy != null) 'fieldOfStudy': fieldOfStudy,
      if (resultType != null) 'resultType': resultType!.toJson(),
      if (result != null) 'result': result,
      if (outOf != null) 'outOf': outOf,
      if (gradeOrDivision != null) 'gradeOrDivision': gradeOrDivision,
      if (startDate != null)
        'startDate': startDate!.toIso8601String().split('T').first,
      if (endDate != null)
        'endDate': endDate!.toIso8601String().split('T').first,
      if (userProfileId != null) 'userProfileId': userProfileId,
    };
  }

  /// Helper method to create a modified copy of this object.
  EducationRequestDTO copyWith({
    EducationLevel? educationLevel,
    String? board,
    String? institution,
    String? fieldOfStudy,
    ResultType? resultType,
    double? result,
    double? outOf,
    String? gradeOrDivision,
    DateTime? startDate,
    DateTime? endDate,
    int? userProfileId,
  }) {
    return EducationRequestDTO(
      educationLevel: educationLevel ?? this.educationLevel,
      board: board ?? this.board,
      institution: institution ?? this.institution,
      fieldOfStudy: fieldOfStudy ?? this.fieldOfStudy,
      resultType: resultType ?? this.resultType,
      result: result ?? this.result,
      outOf: outOf ?? this.outOf,
      gradeOrDivision: gradeOrDivision ?? this.gradeOrDivision,
      startDate: startDate ?? this.startDate,
      endDate: endDate ?? this.endDate,
      userProfileId: userProfileId ?? this.userProfileId,
    );
  }
}