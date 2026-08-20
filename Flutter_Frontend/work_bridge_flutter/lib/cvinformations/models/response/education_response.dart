import 'package:work_bridge_flutter/enums/education_level.dart';
import 'package:work_bridge_flutter/enums/result_type.dart';

class EducationResponseDTO {
  final int? id;
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
  final bool? currentlyStudying;
  final DateTime? createdAt;
  final int? userProfileId;
  final int? userId;
  final String? userName;
  final String? userEmail;

  const EducationResponseDTO({
    this.id,
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
    this.currentlyStudying,
    this.createdAt,
    this.userProfileId,
    this.userId,
    this.userName,
    this.userEmail,
  });

  /// Factory constructor to create [EducationResponseDTO] from a JSON map.
  factory EducationResponseDTO.fromJson(Map<String, dynamic> json) {
    return EducationResponseDTO(
      id: (json['id'] as num?)?.toInt(),
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
      currentlyStudying: json['currentlyStudying'] as bool?,
      createdAt: json['createdAt'] != null
          ? DateTime.tryParse(json['createdAt'] as String)
          : null,
      userProfileId: (json['userProfileId'] as num?)?.toInt(),
      userId: (json['userId'] as num?)?.toInt(),
      userName: json['userName'] as String?,
      userEmail: json['userEmail'] as String?,
    );
  }

  /// Converts this [EducationResponseDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (id != null) 'id': id,
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
      if (currentlyStudying != null) 'currentlyStudying': currentlyStudying,
      if (createdAt != null) 'createdAt': createdAt!.toIso8601String(),
      if (userProfileId != null) 'userProfileId': userProfileId,
      if (userId != null) 'userId': userId,
      if (userName != null) 'userName': userName,
      if (userEmail != null) 'userEmail': userEmail,
    };
  }

  /// Helper method to create a modified copy of this object.
  EducationResponseDTO copyWith({
    int? id,
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
    bool? currentlyStudying,
    DateTime? createdAt,
    int? userProfileId,
    int? userId,
    String? userName,
    String? userEmail,
  }) {
    return EducationResponseDTO(
      id: id ?? this.id,
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
      currentlyStudying: currentlyStudying ?? this.currentlyStudying,
      createdAt: createdAt ?? this.createdAt,
      userProfileId: userProfileId ?? this.userProfileId,
      userId: userId ?? this.userId,
      userName: userName ?? this.userName,
      userEmail: userEmail ?? this.userEmail,
    );
  }
}