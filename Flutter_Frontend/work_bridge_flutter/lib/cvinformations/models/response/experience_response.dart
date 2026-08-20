import 'package:work_bridge_flutter/job/entity/response/job_response.dart';

class ExperienceResponseDTO {
  final int? id;
  final String? companyName;
  final String? position;
  final String? responsibilities;
  final String? achievements;
  final DateTime? startDate;
  final DateTime? endDate;
  final EmploymentType? employmentType;
  final bool? currentlyWorking;
  final DateTime? createdAt;
  final DateTime? updatedAt;
  final int? userProfileId;
  final int? userId;
  final String? userName;
  final String? userEmail;

  const ExperienceResponseDTO({
    this.id,
    this.companyName,
    this.position,
    this.responsibilities,
    this.achievements,
    this.startDate,
    this.endDate,
    this.employmentType,
    this.currentlyWorking,
    this.createdAt,
    this.updatedAt,
    this.userProfileId,
    this.userId,
    this.userName,
    this.userEmail,
  });

  /// Factory constructor to create [ExperienceResponseDTO] from a JSON map.
  factory ExperienceResponseDTO.fromJson(Map<String, dynamic> json) {
    return ExperienceResponseDTO(
      id: (json['id'] as num?)?.toInt(),
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
      currentlyWorking: json['currentlyWorking'] as bool?,
      createdAt: json['createdAt'] != null
          ? DateTime.tryParse(json['createdAt'] as String)
          : null,
      updatedAt: json['updatedAt'] != null
          ? DateTime.tryParse(json['updatedAt'] as String)
          : null,
      userProfileId: (json['userProfileId'] as num?)?.toInt(),
      userId: (json['userId'] as num?)?.toInt(),
      userName: json['userName'] as String?,
      userEmail: json['userEmail'] as String?,
    );
  }

  /// Converts this [ExperienceResponseDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (id != null) 'id': id,
      if (companyName != null) 'companyName': companyName,
      if (position != null) 'position': position,
      if (responsibilities != null) 'responsibilities': responsibilities,
      if (achievements != null) 'achievements': achievements,
      if (startDate != null)
        'startDate': startDate!.toIso8601String().split('T').first,
      if (endDate != null)
        'endDate': endDate!.toIso8601String().split('T').first,
      if (employmentType != null) 'employmentType': employmentType!.toJson(),
      if (currentlyWorking != null) 'currentlyWorking': currentlyWorking,
      if (createdAt != null) 'createdAt': createdAt!.toIso8601String(),
      if (updatedAt != null) 'updatedAt': updatedAt!.toIso8601String(),
      if (userProfileId != null) 'userProfileId': userProfileId,
      if (userId != null) 'userId': userId,
      if (userName != null) 'userName': userName,
      if (userEmail != null) 'userEmail': userEmail,
    };
  }

  /// Helper method to create a modified copy of this object.
  ExperienceResponseDTO copyWith({
    int? id,
    String? companyName,
    String? position,
    String? responsibilities,
    String? achievements,
    DateTime? startDate,
    DateTime? endDate,
    EmploymentType? employmentType,
    bool? currentlyWorking,
    DateTime? createdAt,
    DateTime? updatedAt,
    int? userProfileId,
    int? userId,
    String? userName,
    String? userEmail,
  }) {
    return ExperienceResponseDTO(
      id: id ?? this.id,
      companyName: companyName ?? this.companyName,
      position: position ?? this.position,
      responsibilities: responsibilities ?? this.responsibilities,
      achievements: achievements ?? this.achievements,
      startDate: startDate ?? this.startDate,
      endDate: endDate ?? this.endDate,
      employmentType: employmentType ?? this.employmentType,
      currentlyWorking: currentlyWorking ?? this.currentlyWorking,
      createdAt: createdAt ?? this.createdAt,
      updatedAt: updatedAt ?? this.updatedAt,
      userProfileId: userProfileId ?? this.userProfileId,
      userId: userId ?? this.userId,
      userName: userName ?? this.userName,
      userEmail: userEmail ?? this.userEmail,
    );
  }
}