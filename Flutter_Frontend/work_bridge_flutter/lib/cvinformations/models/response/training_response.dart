import 'package:work_bridge_flutter/enums/training_type.dart';

class TrainingResponseDTO {
  final int? id;
  final String? name;
  final String? description;
  final String? institution;
  final DateTime? startDate;
  final DateTime? endDate;
  final bool? completed;
  final String? duration;
  final String? certificateFile;
  final String? certificateVerificationUrl;
  final String? certificateId;
  final TrainingType? trainingType;
  final DateTime? createdAt;
  final DateTime? updatedAt;
  final int? userProfileId;
  final String? userName;
  final int? userId;
  final String? userEmail;

  const TrainingResponseDTO({
    this.id,
    this.name,
    this.description,
    this.institution,
    this.startDate,
    this.endDate,
    this.completed,
    this.duration,
    this.certificateFile,
    this.certificateVerificationUrl,
    this.certificateId,
    this.trainingType,
    this.createdAt,
    this.updatedAt,
    this.userProfileId,
    this.userName,
    this.userId,
    this.userEmail,
  });

  /// Factory constructor to create [TrainingResponseDTO] from a JSON map.
  factory TrainingResponseDTO.fromJson(Map<String, dynamic> json) {
    return TrainingResponseDTO(
      id: (json['id'] as num?)?.toInt(),
      name: json['name'] as String?,
      description: json['description'] as String?,
      institution: json['institution'] as String?,
      startDate: json['startDate'] != null
          ? DateTime.tryParse(json['startDate'] as String)
          : null,
      endDate: json['endDate'] != null
          ? DateTime.tryParse(json['endDate'] as String)
          : null,
      completed: json['completed'] as bool?,
      duration: json['duration'] as String?,
      certificateFile: json['certificateFile'] as String?,
      certificateVerificationUrl: json['certificateVerificationUrl'] as String?,
      certificateId: json['certificateId'] as String?,
      trainingType: json['trainingType'] != null
          ? TrainingType.fromJson(json['trainingType'] as String)
          : null,
      createdAt: json['createdAt'] != null
          ? DateTime.tryParse(json['createdAt'] as String)
          : null,
      updatedAt: json['updatedAt'] != null
          ? DateTime.tryParse(json['updatedAt'] as String)
          : null,
      userProfileId: (json['userProfileId'] as num?)?.toInt(),
      userName: json['userName'] as String?,
      userId: (json['userId'] as num?)?.toInt(),
      userEmail: json['userEmail'] as String?,
    );
  }

  /// Converts this [TrainingResponseDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (id != null) 'id': id,
      if (name != null) 'name': name,
      if (description != null) 'description': description,
      if (institution != null) 'institution': institution,
      if (startDate != null)
        'startDate':
        '${startDate!.year.toString().padLeft(4, '0')}-${startDate!.month.toString().padLeft(2, '0')}-${startDate!.day.toString().padLeft(2, '0')}',
      if (endDate != null)
        'endDate':
        '${endDate!.year.toString().padLeft(4, '0')}-${endDate!.month.toString().padLeft(2, '0')}-${endDate!.day.toString().padLeft(2, '0')}',
      if (completed != null) 'completed': completed,
      if (duration != null) 'duration': duration,
      if (certificateFile != null) 'certificateFile': certificateFile,
      if (certificateVerificationUrl != null)
        'certificateVerificationUrl': certificateVerificationUrl,
      if (certificateId != null) 'certificateId': certificateId,
      if (trainingType != null) 'trainingType': trainingType!.toJson(),
      if (createdAt != null) 'createdAt': createdAt!.toIso8601String(),
      if (updatedAt != null) 'updatedAt': updatedAt!.toIso8601String(),
      if (userProfileId != null) 'userProfileId': userProfileId,
      if (userName != null) 'userName': userName,
      if (userId != null) 'userId': userId,
      if (userEmail != null) 'userEmail': userEmail,
    };
  }

  /// Helper method to create a modified copy of this object.
  TrainingResponseDTO copyWith({
    int? id,
    String? name,
    String? description,
    String? institution,
    DateTime? startDate,
    DateTime? endDate,
    bool? completed,
    String? duration,
    String? certificateFile,
    String? certificateVerificationUrl,
    String? certificateId,
    TrainingType? trainingType,
    DateTime? createdAt,
    DateTime? updatedAt,
    int? userProfileId,
    String? userName,
    int? userId,
    String? userEmail,
  }) {
    return TrainingResponseDTO(
      id: id ?? this.id,
      name: name ?? this.name,
      description: description ?? this.description,
      institution: institution ?? this.institution,
      startDate: startDate ?? this.startDate,
      endDate: endDate ?? this.endDate,
      completed: completed ?? this.completed,
      duration: duration ?? this.duration,
      certificateFile: certificateFile ?? this.certificateFile,
      certificateVerificationUrl:
      certificateVerificationUrl ?? this.certificateVerificationUrl,
      certificateId: certificateId ?? this.certificateId,
      trainingType: trainingType ?? this.trainingType,
      createdAt: createdAt ?? this.createdAt,
      updatedAt: updatedAt ?? this.updatedAt,
      userProfileId: userProfileId ?? this.userProfileId,
      userName: userName ?? this.userName,
      userId: userId ?? this.userId,
      userEmail: userEmail ?? this.userEmail,
    );
  }
}