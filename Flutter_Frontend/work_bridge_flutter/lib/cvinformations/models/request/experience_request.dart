import 'package:work_bridge_flutter/enums/employment_type.dart';

class ExperienceRequestDTO {
  final String? companyName;
  final String? position;
  final String? responsibilities;
  final String? achievements;
  final DateTime? startDate;
  final DateTime? endDate;
  final EmploymentType? employmentType;
  final int? userProfileId;

  const ExperienceRequestDTO({
    this.companyName,
    this.position,
    this.responsibilities,
    this.achievements,
    this.startDate,
    this.endDate,
    this.employmentType,
    this.userProfileId,
  });

  /// Factory constructor to create [ExperienceRequestDTO] from a JSON map.
  factory ExperienceRequestDTO.fromJson(Map<String, dynamic> json) {
    return ExperienceRequestDTO(
      companyName: json['companyName'] as String?,
      position: json['position'] as String?,
      responsibilities: json['responsibilities'] as String?,
      achievements: json['achievements'] as String?,
      startDate: json['startDate'] != null
          ? DateTime.tryParse(json['startDate'] as String)
          : null,
      endDate: json['endDate'] != null
          ? DateTime.tryParse(json['endDate'] as String)
          : null,
      employmentType: json['employmentType'] != null
          ? EmploymentType.fromJson(json['employmentType'] as String)
          : null,
      userProfileId: (json['userProfileId'] as num?)?.toInt(),
    );
  }

  /// Converts this [ExperienceRequestDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (companyName != null) 'companyName': companyName,
      if (position != null) 'position': position,
      if (responsibilities != null) 'responsibilities': responsibilities,
      if (achievements != null) 'achievements': achievements,
      if (startDate != null)
        'startDate': startDate!.toIso8601String().split('T').first,
      if (endDate != null)
        'endDate': endDate!.toIso8601String().split('T').first,
      if (employmentType != null) 'employmentType': employmentType!.toJson(),
      if (userProfileId != null) 'userProfileId': userProfileId,
    };
  }

  /// Helper method to create a modified copy of this object.
  ExperienceRequestDTO copyWith({
    String? companyName,
    String? position,
    String? responsibilities,
    String? achievements,
    DateTime? startDate,
    DateTime? endDate,
    EmploymentType? employmentType,
    int? userProfileId,
  }) {
    return ExperienceRequestDTO(
      companyName: companyName ?? this.companyName,
      position: position ?? this.position,
      responsibilities: responsibilities ?? this.responsibilities,
      achievements: achievements ?? this.achievements,
      startDate: startDate ?? this.startDate,
      endDate: endDate ?? this.endDate,
      employmentType: employmentType ?? this.employmentType,
      userProfileId: userProfileId ?? this.userProfileId,
    );
  }
}