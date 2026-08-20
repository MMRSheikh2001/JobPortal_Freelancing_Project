import 'package:work_bridge_flutter/enums/gender_type.dart';
import 'package:work_bridge_flutter/enums/job_type.dart';
import 'package:work_bridge_flutter/enums/work_place_type.dart';

class UserProfileRequestDTO {
  final int? userId;
  final String? name;
  final String? phone;
  final String? headline;
  final String? professionalSummary;
  final String? bio;
  final DateTime? dateOfBirth;
  final GenderType? gender;
  final String? nationality;
  final String? religion;
  final String? maritalStatus;
  final String? fatherName;
  final String? motherName;
  final String? nidNumber;
  final String? passportNumber;
  final String? githubLink;
  final String? linkedinLink;
  final String? portfolioWebsite;
  final double? expectedSalary;
  final double? currentSalary;
  final JobType? preferredJobType;
  final WorkPlaceType? preferredWorkplace;
  final String? careerObjective;
  final String? freelancerTitle;
  final int? presentAddressId;
  final String? presentAddressDetails;
  final String? presentAddressPostCode;
  final int? presentAddressPoliceStationId;
  final int? permanentAddressId;
  final String? permanentAddressDetails;
  final String? permanentAddressPostCode;
  final int? permanentAddressPoliceStationId;

  const UserProfileRequestDTO({
    this.userId,
    this.name,
    this.phone,
    this.headline,
    this.professionalSummary,
    this.bio,
    this.dateOfBirth,
    this.gender,
    this.nationality,
    this.religion,
    this.maritalStatus,
    this.fatherName,
    this.motherName,
    this.nidNumber,
    this.passportNumber,
    this.githubLink,
    this.linkedinLink,
    this.portfolioWebsite,
    this.expectedSalary,
    this.currentSalary,
    this.preferredJobType,
    this.preferredWorkplace,
    this.careerObjective,
    this.freelancerTitle,
    this.presentAddressId,
    this.presentAddressDetails,
    this.presentAddressPostCode,
    this.presentAddressPoliceStationId,
    this.permanentAddressId,
    this.permanentAddressDetails,
    this.permanentAddressPostCode,
    this.permanentAddressPoliceStationId,
  });

  /// Factory constructor to create [UserProfileRequestDTO] from a JSON map.
  factory UserProfileRequestDTO.fromJson(Map<String, dynamic> json) {
    return UserProfileRequestDTO(
      userId: (json['userId'] as num?)?.toInt(),
      name: json['name'] as String?,
      phone: json['phone'] as String?,
      headline: json['headline'] as String?,
      professionalSummary: json['professionalSummary'] as String?,
      bio: json['bio'] as String?,
      dateOfBirth: json['dateOfBirth'] != null
          ? DateTime.tryParse(json['dateOfBirth'] as String)
          : null,
      gender: json['gender'] != null
          ? GenderType.fromJson(json['gender'] as String)
          : null,
      nationality: json['nationality'] as String?,
      religion: json['religion'] as String?,
      maritalStatus: json['maritalStatus'] as String?,
      fatherName: json['fatherName'] as String?,
      motherName: json['motherName'] as String?,
      nidNumber: json['nidNumber'] as String?,
      passportNumber: json['passportNumber'] as String?,
      githubLink: json['githubLink'] as String?,
      linkedinLink: json['linkedinLink'] as String?,
      portfolioWebsite: json['portfolioWebsite'] as String?,
      expectedSalary: (json['expectedSalary'] as num?)?.toDouble(),
      currentSalary: (json['currentSalary'] as num?)?.toDouble(),
      preferredJobType: json['preferredJobType'] != null
          ? JobType.fromJson(json['preferredJobType'] as String)
          : null,
      preferredWorkplace: json['preferredWorkplace'] != null
          ? WorkPlaceType.fromJson(json['preferredWorkplace'] as String)
          : null,
      careerObjective: json['careerObjective'] as String?,
      freelancerTitle: json['freelancerTitle'] as String?,
      presentAddressId: (json['presentAddressId'] as num?)?.toInt(),
      presentAddressDetails: json['presentAddressDetails'] as String?,
      presentAddressPostCode: json['presentAddressPostCode'] as String?,
      presentAddressPoliceStationId:
      (json['presentAddressPoliceStationId'] as num?)?.toInt(),
      permanentAddressId: (json['permanentAddressId'] as num?)?.toInt(),
      permanentAddressDetails: json['permanentAddressDetails'] as String?,
      permanentAddressPostCode: json['permanentAddressPostCode'] as String?,
      permanentAddressPoliceStationId:
      (json['permanentAddressPoliceStationId'] as num?)?.toInt(),
    );
  }

  /// Converts this [UserProfileRequestDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (userId != null) 'userId': userId,
      if (name != null) 'name': name,
      if (phone != null) 'phone': phone,
      if (headline != null) 'headline': headline,
      if (professionalSummary != null)
        'professionalSummary': professionalSummary,
      if (bio != null) 'bio': bio,
      if (dateOfBirth != null)
        'dateOfBirth': dateOfBirth!.toIso8601String().split('T').first,
      if (gender != null) 'gender': gender!.toJson(),
      if (nationality != null) 'nationality': nationality,
      if (religion != null) 'religion': religion,
      if (maritalStatus != null) 'maritalStatus': maritalStatus,
      if (fatherName != null) 'fatherName': fatherName,
      if (motherName != null) 'motherName': motherName,
      if (nidNumber != null) 'nidNumber': nidNumber,
      if (passportNumber != null) 'passportNumber': passportNumber,
      if (githubLink != null) 'githubLink': githubLink,
      if (linkedinLink != null) 'linkedinLink': linkedinLink,
      if (portfolioWebsite != null) 'portfolioWebsite': portfolioWebsite,
      if (expectedSalary != null) 'expectedSalary': expectedSalary,
      if (currentSalary != null) 'currentSalary': currentSalary,
      if (preferredJobType != null)
        'preferredJobType': preferredJobType!.toJson(),
      if (preferredWorkplace != null)
        'preferredWorkplace': preferredWorkplace!.toJson(),
      if (careerObjective != null) 'careerObjective': careerObjective,
      if (freelancerTitle != null) 'freelancerTitle': freelancerTitle,
      if (presentAddressId != null) 'presentAddressId': presentAddressId,
      if (presentAddressDetails != null)
        'presentAddressDetails': presentAddressDetails,
      if (presentAddressPostCode != null)
        'presentAddressPostCode': presentAddressPostCode,
      if (presentAddressPoliceStationId != null)
        'presentAddressPoliceStationId': presentAddressPoliceStationId,
      if (permanentAddressId != null) 'permanentAddressId': permanentAddressId,
      if (permanentAddressDetails != null)
        'permanentAddressDetails': permanentAddressDetails,
      if (permanentAddressPostCode != null)
        'permanentAddressPostCode': permanentAddressPostCode,
      if (permanentAddressPoliceStationId != null)
        'permanentAddressPoliceStationId': permanentAddressPoliceStationId,
    };
  }

  /// Helper method to create a modified copy of this object.
  UserProfileRequestDTO copyWith({
    int? userId,
    String? name,
    String? phone,
    String? headline,
    String? professionalSummary,
    String? bio,
    DateTime? dateOfBirth,
    GenderType? gender,
    String? nationality,
    String? religion,
    String? maritalStatus,
    String? fatherName,
    String? motherName,
    String? nidNumber,
    String? passportNumber,
    String? githubLink,
    String? linkedinLink,
    String? portfolioWebsite,
    double? expectedSalary,
    double? currentSalary,
    JobType? preferredJobType,
    WorkPlaceType? preferredWorkplace,
    String? careerObjective,
    String? freelancerTitle,
    int? presentAddressId,
    String? presentAddressDetails,
    String? presentAddressPostCode,
    int? presentAddressPoliceStationId,
    int? permanentAddressId,
    String? permanentAddressDetails,
    String? permanentAddressPostCode,
    int? permanentAddressPoliceStationId,
  }) {
    return UserProfileRequestDTO(
      userId: userId ?? this.userId,
      name: name ?? this.name,
      phone: phone ?? this.phone,
      headline: headline ?? this.headline,
      professionalSummary: professionalSummary ?? this.professionalSummary,
      bio: bio ?? this.bio,
      dateOfBirth: dateOfBirth ?? this.dateOfBirth,
      gender: gender ?? this.gender,
      nationality: nationality ?? this.nationality,
      religion: religion ?? this.religion,
      maritalStatus: maritalStatus ?? this.maritalStatus,
      fatherName: fatherName ?? this.fatherName,
      motherName: motherName ?? this.motherName,
      nidNumber: nidNumber ?? this.nidNumber,
      passportNumber: passportNumber ?? this.passportNumber,
      githubLink: githubLink ?? this.githubLink,
      linkedinLink: linkedinLink ?? this.linkedinLink,
      portfolioWebsite: portfolioWebsite ?? this.portfolioWebsite,
      expectedSalary: expectedSalary ?? this.expectedSalary,
      currentSalary: currentSalary ?? this.currentSalary,
      preferredJobType: preferredJobType ?? this.preferredJobType,
      preferredWorkplace: preferredWorkplace ?? this.preferredWorkplace,
      careerObjective: careerObjective ?? this.careerObjective,
      freelancerTitle: freelancerTitle ?? this.freelancerTitle,
      presentAddressId: presentAddressId ?? this.presentAddressId,
      presentAddressDetails:
      presentAddressDetails ?? this.presentAddressDetails,
      presentAddressPostCode:
      presentAddressPostCode ?? this.presentAddressPostCode,
      presentAddressPoliceStationId:
      presentAddressPoliceStationId ?? this.presentAddressPoliceStationId,
      permanentAddressId: permanentAddressId ?? this.permanentAddressId,
      permanentAddressDetails:
      permanentAddressDetails ?? this.permanentAddressDetails,
      permanentAddressPostCode:
      permanentAddressPostCode ?? this.permanentAddressPostCode,
      permanentAddressPoliceStationId: permanentAddressPoliceStationId ??
          this.permanentAddressPoliceStationId,
    );
  }
}