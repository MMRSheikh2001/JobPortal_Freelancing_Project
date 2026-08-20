import 'package:work_bridge_flutter/enums/work_place_type.dart';

enum EmploymentType {
  fullTime,
  partTime,
  contract,
  internship,
  freelance;

  String toJson() {
    switch (this) {
      case EmploymentType.fullTime:
        return 'Full_Time';
      case EmploymentType.partTime:
        return 'Part_Time';
      case EmploymentType.contract:
        return 'Contract';
      case EmploymentType.internship:
        return 'Internship';
      case EmploymentType.freelance:
        return 'Freelance';
    }
  }

  static EmploymentType? fromJson(String? value) {
    if (value == null) return null;
    switch (value.toUpperCase()) {
      case 'FULL_TIME':
        return EmploymentType.fullTime;
      case 'PART_TIME':
        return EmploymentType.partTime;
      case 'CONTRACT':
        return EmploymentType.contract;
      case 'INTERNSHIP':
        return EmploymentType.internship;
      case 'FREELANCE':
        return EmploymentType.freelance;
      default:
        return null;
    }
  }
}



class JobResponseDTO {
  final int? id;
  final String? title;
  final String? jobDescription;
  final String? jobResponsibilities;
  final String? educationalRequirements;
  final String? experienceRequirements;
  final int? minExperience;
  final int? maxExperience;
  final String? additionalRequirements;
  final String? benefits;
  final double? salaryMin;
  final double? salaryMax;
  final bool? isNegotiable;
  final DateTime? applicationDeadline;
  final bool? isActive;
  final int? vacancy;
  final EmploymentType? employmentType;
  final WorkPlaceType? workPlaceType;
  final DateTime? createdAt;
  final DateTime? updatedAt;

  // Company details
  final int? companyProfileId;
  final int? userId;
  final String? userEmail;
  final String? companyName;
  final String? companyEmail;
  final String? companyPhone;
  final String? companyDescription;
  final String? companyWebsite;
  final String? companyLogo;

  // Location details
  final int? locationCountryId;
  final String? locationCountryName;
  final String? locationCountryCode;
  final int? locationDivisionId;
  final String? locationDivisionName;
  final int? locationDistrictId;
  final String? locationDistrictName;
  final int? locationPoliceStationId;
  final String? locationPoliceStationName;

  // Category
  final int? categoryId;
  final String? categoryName;

  // AI Integration
  final bool? aiScreeningEnabled;
  final bool? aiCvScreeningEnabled;
  final bool? aiInterviewEnabled;
  final int? aiMatchThreshold;
  final int? aiQuestionCount;
  final int? aiShortlistCount;
  final int? aiDeadlineDays;

  const JobResponseDTO({
    this.id,
    this.title,
    this.jobDescription,
    this.jobResponsibilities,
    this.educationalRequirements,
    this.experienceRequirements,
    this.minExperience,
    this.maxExperience,
    this.additionalRequirements,
    this.benefits,
    this.salaryMin,
    this.salaryMax,
    this.isNegotiable,
    this.applicationDeadline,
    this.isActive,
    this.vacancy,
    this.employmentType,
    this.workPlaceType,
    this.createdAt,
    this.updatedAt,
    this.companyProfileId,
    this.userId,
    this.userEmail,
    this.companyName,
    this.companyEmail,
    this.companyPhone,
    this.companyDescription,
    this.companyWebsite,
    this.companyLogo,
    this.locationCountryId,
    this.locationCountryName,
    this.locationCountryCode,
    this.locationDivisionId,
    this.locationDivisionName,
    this.locationDistrictId,
    this.locationDistrictName,
    this.locationPoliceStationId,
    this.locationPoliceStationName,
    this.categoryId,
    this.categoryName,
    this.aiScreeningEnabled,
    this.aiCvScreeningEnabled,
    this.aiInterviewEnabled,
    this.aiMatchThreshold,
    this.aiQuestionCount,
    this.aiShortlistCount,
    this.aiDeadlineDays,
  });

  /// Factory constructor to create a [JobResponseDTO] instance from a JSON map.
  factory JobResponseDTO.fromJson(Map<String, dynamic> json) {
    return JobResponseDTO(
      id: (json['id'] as num?)?.toInt(),
      title: json['title'] as String?,
      jobDescription: json['jobDescription'] as String?,
      jobResponsibilities: json['jobResponsibilities'] as String?,
      educationalRequirements: json['educationalRequirements'] as String?,
      experienceRequirements: json['experienceRequirements'] as String?,
      minExperience: (json['minExperience'] as num?)?.toInt(),
      maxExperience: (json['maxExperience'] as num?)?.toInt(),
      additionalRequirements: json['additionalRequirements'] as String?,
      benefits: json['benefits'] as String?,
      salaryMin: (json['salaryMin'] as num?)?.toDouble(),
      salaryMax: (json['salaryMax'] as num?)?.toDouble(),
      isNegotiable: json['isNegotiable'] as bool?,
      applicationDeadline: json['applicationDeadline'] != null
          ? DateTime.tryParse(json['applicationDeadline'] as String)
          : null,
      isActive: json['isActive'] as bool?,
      vacancy: (json['vacancy'] as num?)?.toInt(),
      employmentType: json['employmentType'] != null
          ? EmploymentType.fromJson(json['employmentType'] as String)
          : null,
      workPlaceType: json['workPlaceType'] != null
          ? WorkPlaceType.fromJson(json['workPlaceType'] as String)
          : null,
      createdAt: json['createdAt'] != null
          ? DateTime.tryParse(json['createdAt'] as String)
          : null,
      updatedAt: json['updatedAt'] != null
          ? DateTime.tryParse(json['updatedAt'] as String)
          : null,
      companyProfileId: (json['companyProfileId'] as num?)?.toInt(),
      userId: (json['userId'] as num?)?.toInt(),
      userEmail: json['userEmail'] as String?,
      companyName: json['companyName'] as String?,
      companyEmail: json['companyEmail'] as String?,
      companyPhone: json['companyPhone'] as String?,
      companyDescription: json['companyDescription'] as String?,
      companyWebsite: json['companyWebsite'] as String?,
      companyLogo: json['companyLogo'] as String?,
      locationCountryId: (json['locationCountryId'] as num?)?.toInt(),
      locationCountryName: json['locationCountryName'] as String?,
      locationCountryCode: json['locationCountryCode'] as String?,
      locationDivisionId: (json['locationDivisionId'] as num?)?.toInt(),
      locationDivisionName: json['locationDivisionName'] as String?,
      locationDistrictId: (json['locationDistrictId'] as num?)?.toInt(),
      locationDistrictName: json['locationDistrictName'] as String?,
      locationPoliceStationId: (json['locationPoliceStationId'] as num?)?.toInt(),
      locationPoliceStationName: json['locationPoliceStationName'] as String?,
      categoryId: (json['categoryId'] as num?)?.toInt(),
      categoryName: json['categoryName'] as String?,
      aiScreeningEnabled: json['aiScreeningEnabled'] as bool?,
      aiCvScreeningEnabled: json['aiCvScreeningEnabled'] as bool?,
      aiInterviewEnabled: json['aiInterviewEnabled'] as bool?,
      aiMatchThreshold: (json['aiMatchThreshold'] as num?)?.toInt(),
      aiQuestionCount: (json['aiQuestionCount'] as num?)?.toInt(),
      aiShortlistCount: (json['aiShortlistCount'] as num?)?.toInt(),
      aiDeadlineDays: (json['aiDeadlineDays'] as num?)?.toInt(),
    );
  }

  /// Converts this [JobResponseDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (id != null) 'id': id,
      if (title != null) 'title': title,
      if (jobDescription != null) 'jobDescription': jobDescription,
      if (jobResponsibilities != null) 'jobResponsibilities': jobResponsibilities,
      if (educationalRequirements != null)
        'educationalRequirements': educationalRequirements,
      if (experienceRequirements != null)
        'experienceRequirements': experienceRequirements,
      if (minExperience != null) 'minExperience': minExperience,
      if (maxExperience != null) 'maxExperience': maxExperience,
      if (additionalRequirements != null)
        'additionalRequirements': additionalRequirements,
      if (benefits != null) 'benefits': benefits,
      if (salaryMin != null) 'salaryMin': salaryMin,
      if (salaryMax != null) 'salaryMax': salaryMax,
      if (isNegotiable != null) 'isNegotiable': isNegotiable,
      if (applicationDeadline != null)
        'applicationDeadline':
        applicationDeadline!.toIso8601String().split('T').first,
      if (isActive != null) 'isActive': isActive,
      if (vacancy != null) 'vacancy': vacancy,
      if (employmentType != null) 'employmentType': employmentType!.toJson(),
      if (workPlaceType != null) 'workPlaceType': workPlaceType!.toJson(),
      if (createdAt != null) 'createdAt': createdAt!.toIso8601String(),
      if (updatedAt != null) 'updatedAt': updatedAt!.toIso8601String(),
      if (companyProfileId != null) 'companyProfileId': companyProfileId,
      if (userId != null) 'userId': userId,
      if (userEmail != null) 'userEmail': userEmail,
      if (companyName != null) 'companyName': companyName,
      if (companyEmail != null) 'companyEmail': companyEmail,
      if (companyPhone != null) 'companyPhone': companyPhone,
      if (companyDescription != null) 'companyDescription': companyDescription,
      if (companyWebsite != null) 'companyWebsite': companyWebsite,
      if (companyLogo != null) 'companyLogo': companyLogo,
      if (locationCountryId != null) 'locationCountryId': locationCountryId,
      if (locationCountryName != null) 'locationCountryName': locationCountryName,
      if (locationCountryCode != null) 'locationCountryCode': locationCountryCode,
      if (locationDivisionId != null) 'locationDivisionId': locationDivisionId,
      if (locationDivisionName != null)
        'locationDivisionName': locationDivisionName,
      if (locationDistrictId != null) 'locationDistrictId': locationDistrictId,
      if (locationDistrictName != null)
        'locationDistrictName': locationDistrictName,
      if (locationPoliceStationId != null)
        'locationPoliceStationId': locationPoliceStationId,
      if (locationPoliceStationName != null)
        'locationPoliceStationName': locationPoliceStationName,
      if (categoryId != null) 'categoryId': categoryId,
      if (categoryName != null) 'categoryName': categoryName,
      if (aiScreeningEnabled != null) 'aiScreeningEnabled': aiScreeningEnabled,
      if (aiCvScreeningEnabled != null)
        'aiCvScreeningEnabled': aiCvScreeningEnabled,
      if (aiInterviewEnabled != null) 'aiInterviewEnabled': aiInterviewEnabled,
      if (aiMatchThreshold != null) 'aiMatchThreshold': aiMatchThreshold,
      if (aiQuestionCount != null) 'aiQuestionCount': aiQuestionCount,
      if (aiShortlistCount != null) 'aiShortlistCount': aiShortlistCount,
      if (aiDeadlineDays != null) 'aiDeadlineDays': aiDeadlineDays,
    };
  }

  /// Helper method to create a modified copy of this object.
  JobResponseDTO copyWith({
    int? id,
    String? title,
    String? jobDescription,
    String? jobResponsibilities,
    String? educationalRequirements,
    String? experienceRequirements,
    int? minExperience,
    int? maxExperience,
    String? additionalRequirements,
    String? benefits,
    double? salaryMin,
    double? salaryMax,
    bool? isNegotiable,
    DateTime? applicationDeadline,
    bool? isActive,
    int? vacancy,
    EmploymentType? employmentType,
    WorkPlaceType? workPlaceType,
    DateTime? createdAt,
    DateTime? updatedAt,
    int? companyProfileId,
    int? userId,
    String? userEmail,
    String? companyName,
    String? companyEmail,
    String? companyPhone,
    String? companyDescription,
    String? companyWebsite,
    String? companyLogo,
    int? locationCountryId,
    String? locationCountryName,
    String? locationCountryCode,
    int? locationDivisionId,
    String? locationDivisionName,
    int? locationDistrictId,
    String? locationDistrictName,
    int? locationPoliceStationId,
    String? locationPoliceStationName,
    int? categoryId,
    String? categoryName,
    bool? aiScreeningEnabled,
    bool? aiCvScreeningEnabled,
    bool? aiInterviewEnabled,
    int? aiMatchThreshold,
    int? aiQuestionCount,
    int? aiShortlistCount,
    int? aiDeadlineDays,
  }) {
    return JobResponseDTO(
      id: id ?? this.id,
      title: title ?? this.title,
      jobDescription: jobDescription ?? this.jobDescription,
      jobResponsibilities: jobResponsibilities ?? this.jobResponsibilities,
      educationalRequirements:
      educationalRequirements ?? this.educationalRequirements,
      experienceRequirements:
      experienceRequirements ?? this.experienceRequirements,
      minExperience: minExperience ?? this.minExperience,
      maxExperience: maxExperience ?? this.maxExperience,
      additionalRequirements:
      additionalRequirements ?? this.additionalRequirements,
      benefits: benefits ?? this.benefits,
      salaryMin: salaryMin ?? this.salaryMin,
      salaryMax: salaryMax ?? this.salaryMax,
      isNegotiable: isNegotiable ?? this.isNegotiable,
      applicationDeadline: applicationDeadline ?? this.applicationDeadline,
      isActive: isActive ?? this.isActive,
      vacancy: vacancy ?? this.vacancy,
      employmentType: employmentType ?? this.employmentType,
      workPlaceType: workPlaceType ?? this.workPlaceType,
      createdAt: createdAt ?? this.createdAt,
      updatedAt: updatedAt ?? this.updatedAt,
      companyProfileId: companyProfileId ?? this.companyProfileId,
      userId: userId ?? this.userId,
      userEmail: userEmail ?? this.userEmail,
      companyName: companyName ?? this.companyName,
      companyEmail: companyEmail ?? this.companyEmail,
      companyPhone: companyPhone ?? this.companyPhone,
      companyDescription: companyDescription ?? this.companyDescription,
      companyWebsite: companyWebsite ?? this.companyWebsite,
      companyLogo: companyLogo ?? this.companyLogo,
      locationCountryId: locationCountryId ?? this.locationCountryId,
      locationCountryName: locationCountryName ?? this.locationCountryName,
      locationCountryCode: locationCountryCode ?? this.locationCountryCode,
      locationDivisionId: locationDivisionId ?? this.locationDivisionId,
      locationDivisionName: locationDivisionName ?? this.locationDivisionName,
      locationDistrictId: locationDistrictId ?? this.locationDistrictId,
      locationDistrictName: locationDistrictName ?? this.locationDistrictName,
      locationPoliceStationId:
      locationPoliceStationId ?? this.locationPoliceStationId,
      locationPoliceStationName:
      locationPoliceStationName ?? this.locationPoliceStationName,
      categoryId: categoryId ?? this.categoryId,
      categoryName: categoryName ?? this.categoryName,
      aiScreeningEnabled: aiScreeningEnabled ?? this.aiScreeningEnabled,
      aiCvScreeningEnabled: aiCvScreeningEnabled ?? this.aiCvScreeningEnabled,
      aiInterviewEnabled: aiInterviewEnabled ?? this.aiInterviewEnabled,
      aiMatchThreshold: aiMatchThreshold ?? this.aiMatchThreshold,
      aiQuestionCount: aiQuestionCount ?? this.aiQuestionCount,
      aiShortlistCount: aiShortlistCount ?? this.aiShortlistCount,
      aiDeadlineDays: aiDeadlineDays ?? this.aiDeadlineDays,
    );
  }
}