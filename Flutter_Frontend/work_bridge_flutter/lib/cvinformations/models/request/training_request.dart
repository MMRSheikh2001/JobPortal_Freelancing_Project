import 'package:work_bridge_flutter/enums/training_type.dart';

class TrainingRequestDTO {
  final String? name;
  final String? description;
  final String? institution;
  final DateTime? startDate;
  final DateTime? endDate;
  final String? duration;
  final String? certificateVerificationUrl;
  final String? certificateId;
  final TrainingType? trainingType;
  final int? userProfileId;

  const TrainingRequestDTO({
    this.name,
    this.description,
    this.institution,
    this.startDate,
    this.endDate,
    this.duration,
    this.certificateVerificationUrl,
    this.certificateId,
    this.trainingType,
    this.userProfileId,
  });

  /// Factory constructor to create [TrainingRequestDTO] from a JSON map.
  factory TrainingRequestDTO.fromJson(Map<String, dynamic> json) {
    return TrainingRequestDTO(
      name: json['name'] as String?,
      description: json['description'] as String?,
      institution: json['institution'] as String?,
      startDate: json['startDate'] != null
          ? DateTime.tryParse(json['startDate'] as String)
          : null,
      endDate: json['endDate'] != null
          ? DateTime.tryParse(json['endDate'] as String)
          : null,
      duration: json['duration'] as String?,
      certificateVerificationUrl: json['certificateVerificationUrl'] as String?,
      certificateId: json['certificateId'] as String?,
      trainingType: json['trainingType'] != null
          ? TrainingType.fromJson(json['trainingType'] as String)
          : null,
      userProfileId: (json['userProfileId'] as num?)?.toInt(),
    );
  }

  /// Converts this [TrainingRequestDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (name != null) 'name': name,
      if (description != null) 'description': description,
      if (institution != null) 'institution': institution,
      if (startDate != null)
        'startDate':
        '${startDate!.year.toString().padLeft(4, '0')}-${startDate!.month.toString().padLeft(2, '0')}-${startDate!.day.toString().padLeft(2, '0')}',
      if (endDate != null)
        'endDate':
        '${endDate!.year.toString().padLeft(4, '0')}-${endDate!.month.toString().padLeft(2, '0')}-${endDate!.day.toString().padLeft(2, '0')}',
      if (duration != null) 'duration': duration,
      if (certificateVerificationUrl != null)
        'certificateVerificationUrl': certificateVerificationUrl,
      if (certificateId != null) 'certificateId': certificateId,
      if (trainingType != null) 'trainingType': trainingType!.toJson(),
      if (userProfileId != null) 'userProfileId': userProfileId,
    };
  }

  /// Helper method to create a modified copy of this object.
  TrainingRequestDTO copyWith({
    String? name,
    String? description,
    String? institution,
    DateTime? startDate,
    DateTime? endDate,
    String? duration,
    String? certificateVerificationUrl,
    String? certificateId,
    TrainingType? trainingType,
    int? userProfileId,
  }) {
    return TrainingRequestDTO(
      name: name ?? this.name,
      description: description ?? this.description,
      institution: institution ?? this.institution,
      startDate: startDate ?? this.startDate,
      endDate: endDate ?? this.endDate,
      duration: duration ?? this.duration,
      certificateVerificationUrl:
      certificateVerificationUrl ?? this.certificateVerificationUrl,
      certificateId: certificateId ?? this.certificateId,
      trainingType: trainingType ?? this.trainingType,
      userProfileId: userProfileId ?? this.userProfileId,
    );
  }
}